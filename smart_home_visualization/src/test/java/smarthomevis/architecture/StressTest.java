package smarthomevis.architecture;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.logic.Device;

import java.io.IOException;

// This test needs MongoDB running to pass
@Ignore
public class StressTest {

    private static final int NUMBER_OF_RUNS = 10;
    private static final int NUMBER_OF_DEVICES = 1000;

    @Test
    public void testWithRestlet() throws IOException {
        String testUrl = "http://localhost:8183/smarthome/devices/";
        ClientResource client = new ClientResource(testUrl);
        client.delete();
        Assert.assertEquals(0, new Repository<>(Device.class).count());

        String deviceJson = "{\"name\":\"DeviceName\"}";

        long accu = 0l;
        long runStartTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            long createStartTime = System.currentTimeMillis();
            for (int j = 0; j < NUMBER_OF_DEVICES; j++) {
                client.post(deviceJson).getText();
            }
            long createTime = System.currentTimeMillis() - createStartTime;
            System.out.println(String.format("Created %s devices in %s ms.", NUMBER_OF_DEVICES, createTime));
            accu += createTime;
        }
        long runTime = System.currentTimeMillis() - runStartTime;
        System.out.println(String.format("Finished run in %s ms.", runTime));
        System.out.println(String.format("The average time for each operation was %s ms.", accu/NUMBER_OF_RUNS));

        Assert.assertEquals(NUMBER_OF_DEVICES * NUMBER_OF_RUNS, new Repository<>(Device.class).count());
        client.delete();
    }

    @Test
    public void testOnlyDatabase() throws IOException {
        Repository<Device> repository = new Repository<>(Device.class);
        repository.deleteAll();
        Assert.assertEquals(0, repository.count());

        long accu = 0l;
        long runStartTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            long createStartTime = System.currentTimeMillis();
            for (int j = 0; j < NUMBER_OF_DEVICES; j++) {
                Device device = new Device();
                device.setName("DeviceName");
                repository.save(device);
            }
            long createTime = System.currentTimeMillis() - createStartTime;
            System.out.println(String.format("Created %s devices in %s ms.", NUMBER_OF_DEVICES, createTime));
            accu += createTime;
        }
        long runTime = System.currentTimeMillis() - runStartTime;
        System.out.println(String.format("Finished run in %s ms.", runTime));
        System.out.println(String.format("The average time for each operation was %s ms.", accu/NUMBER_OF_RUNS));

        Assert.assertEquals(NUMBER_OF_DEVICES * NUMBER_OF_RUNS, repository.count());
        repository.deleteAll();
    }
}
