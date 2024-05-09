package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbiir.tristar.config.Workload;
import org.dbiir.tristar.config.WorkloadLoader;
import org.junit.jupiter.api.Test;

public class TestLoader {

    @Test
    public void loadWorkload() {
        String filepath = "config/workloads/smallbank.xml";
        WorkloadLoader loader = new WorkloadLoader();
        Workload w = loader.load(filepath);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(w);
            System.out.println(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
