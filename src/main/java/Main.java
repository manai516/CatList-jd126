import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class Main {

    public static final String DEMO_API_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static final ObjectMapper mapper = new ObjectMapper();

    static void main(String[] args) {
        try {
            CloseableHttpClient httpClient =createHttpClient();
            HttpGet request = new HttpGet(DEMO_API_URL);
            request.setHeader("Accept", "application/json");
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // JSON в список
                List<Cat> cats = mapper.readValue(jsonResponse, new TypeReference<List<Cat>>() {});
                System.out.println("Получено фактов " + cats.size());

                List<Cat> filters = cats.stream()
                        .filter(cat -> cat.upvotes() !=null && cat.upvotes() > 0)
                        .toList();
                System.out.println("Фактов с ненулевым upvotes: " + filters.size());
                filters.forEach(cat -> System.out.println(cat.text()));

            } finally {
                response.close();
                httpClient.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setUserAgent("Lecture-Demo-Client/1.0")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }
}
