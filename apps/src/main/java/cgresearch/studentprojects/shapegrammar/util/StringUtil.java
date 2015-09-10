package cgresearch.studentprojects.shapegrammar.util;

/**
 * The Class StringUtil contains static function to work with strings.
 * @author Thorben Watzl
 */
public class StringUtil {
	
	/**
	 * String between.
	 *
	 * @param string the string
	 * @param first the first
	 * @param end the end
	 * @return the string
	 */
	public static String stringBetween(String string, String first,String end){
		int pathStartIndex = string.indexOf(first) + 1;
		int pathEntIndex = string.indexOf(end, pathStartIndex);
		return string.substring(pathStartIndex, pathEntIndex);
	}
}
