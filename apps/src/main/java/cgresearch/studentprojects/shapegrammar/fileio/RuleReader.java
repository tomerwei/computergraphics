package cgresearch.studentprojects.shapegrammar.fileio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.studentprojects.shapegrammar.datastructures.rules.GrammarRule;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualMethod;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;
import cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair;
import cgresearch.studentprojects.shapegrammar.util.RandomRulenameValuePair;
import cgresearch.studentprojects.shapegrammar.util.RulenameValuePair;
import cgresearch.studentprojects.shapegrammar.util.StringUtil;

/**
 * The Class RuleReader read and parse the rule file.
 * 
 * @author Thorben Watzl
 */
public class RuleReader {

	/** The input stream. */
	private InputStream inputStream;

	/** The buffered reader. */
	private BufferedReader bufferedReader;

	/**
	 * Read rules from file.
	 *
	 * @param path
	 *            the path
	 * @return the map
	 */
	public Map<String, GrammarRule> readRulesFromFile(String path, BuildingSettings settings) {
		try {
			Map<String, GrammarRule> rules = new HashMap<String, GrammarRule>();
			inputStream = new FileInputStream(path);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			List<String> ruleStringList;
			while ((ruleStringList = this.readNextRuleAsStringList()) != null) {
				if (ruleStringList.get(0).contains("import")) {
					String file = StringUtil.stringBetween(ruleStringList.get(0), "\"", "\"");
					RuleReader ruleReader = new RuleReader();
					rules.putAll(ruleReader.readRulesFromFile(
							ResourcesLocator.getInstance().getPathToResource(settings.getBaseDirectory() + file),
							settings));
				} else if (ruleStringList.get(0).contains("-->")) {
					createRuleFromStringList(ruleStringList, rules, settings);
				} else {
					Logger.getInstance().exception("The Grammar is Wrong!",
							new IOException("The grammar is wrong in line: " + ruleStringList.get(0)));
					return null;
				}
			}
			Logger.getInstance().message("Rule File \"" + path + "\" read!");
			bufferedReader.close();
			return rules;
		} catch (IOException e) {
			Logger.getInstance().exception("Failed to read the Rule file", e);
			return null;
		}
	}

	/**
	 * Creates the rule from string list.
	 *
	 * @param ruleStringList
	 *            the rule string list
	 * @param rules
	 *            the rules
	 */
	private void createRuleFromStringList(List<String> ruleStringList, Map<String, GrammarRule> rules,
			BuildingSettings settings) {
		GrammarRule rule = new GrammarRule();
		String name = ruleStringList.get(0);
		name = name.replace("-->", "");
		name = name.trim();
		rule.setName(name);
		ruleStringList.remove(0);
		for (String ruleLine : ruleStringList) {
			if (ruleLine.contains("texture")) {
				String textureFilename = settings.getBaseDirectory()
						+ StringUtil.stringBetween(ruleLine, "\"", "\"").trim();
				String textureId = textureFilename.substring(textureFilename.lastIndexOf("/") + 1,
						textureFilename.length());
				if (!ResourceManager.getTextureManagerInstance().idExists(textureId)) {
					CgTexture cgTexture = new CgTexture(textureFilename);
					ResourceManager.getTextureManagerInstance().addResource(textureId, cgTexture);
				}
				rule.setTexture(textureId);
			} else if (ruleLine.contains("model3d")) {
				String model3d = StringUtil.stringBetween(ruleLine, "\"", "\"");
				String model3dRotation = StringUtil.stringBetween(ruleLine, ",", "�");
				model3d = model3d.trim();
				model3dRotation = model3dRotation.trim();
				rule.setModel3d(model3d);
				rule.setModel3dRotation(model3dRotation);
			} else if (ruleLine.contains("split") || ruleLine.contains("extrude")) {
				String splitParam = StringUtil.stringBetween(ruleLine, "(", ")").trim();
				String splitDeleg = StringUtil.stringBetween(ruleLine, "{", "}").trim();
				List<IRulenameValuePair> degationList = new ArrayList<IRulenameValuePair>();
				double probability = 100;
				for (String splitedDeleg : splitDeleg.split("\\|")) {
					if (splitedDeleg.contains("%")) {
						String stringProbability = StringUtil.stringBetween(splitedDeleg, "[", "]");
						probability = Double.parseDouble(stringProbability.replace("%", "").trim());
						splitedDeleg = splitedDeleg.substring(0, splitedDeleg.indexOf("["));
					}
					String[] splitRuleDeleg = splitedDeleg.split(":");
					List<String> values = new ArrayList<String>();
					if (splitRuleDeleg[0].contains(",")) {
						String[] splitValues = splitRuleDeleg[0].split(",");
						for (String value : splitValues) {
							value = value.trim();
							try {
								double scaledValue = Double.parseDouble(value.trim());
								values.add(new Double(scaledValue).toString());
							} catch (NumberFormatException e) {
								values.add(value);
							}
						}
					} else {
						values.add(splitRuleDeleg[0].trim());
					}
					IRulenameValuePair rulenameValuePair;
					if (probability == 100) {
						rulenameValuePair = new RulenameValuePair(splitRuleDeleg[1].trim(), values);
					} else {
						rulenameValuePair = new RandomRulenameValuePair(splitRuleDeleg[1].trim(), values, probability);
					}
					degationList.add(rulenameValuePair);
				}
				boolean repeat = false;
				if (ruleLine.contains("*")) {
					repeat = true;
				}
				String splitOrExtrude = ruleLine.substring(0, ruleLine.indexOf("(")).trim();
				rule.setMethod(new VirtualMethod(splitOrExtrude, splitParam, degationList, repeat));
			}
		}
		rules.put(rule.getName(), rule);
	}

	/**
	 * Read next rule as string list.
	 *
	 * @return the list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private List<String> readNextRuleAsStringList() throws IOException {
		String line;
		List<String> ruleBlock = new ArrayList<String>();
		while ((line = bufferedReader.readLine()) != null) {
			if (line.trim().equals("")) {
				continue;
			}
			ruleBlock.add(line);
			if (line.contains(";")) {
				break;
			}
		}
		if (line != null) {
			return ruleBlock;
		}
		return null;
	}

}
