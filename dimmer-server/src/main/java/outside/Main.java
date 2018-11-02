package outside;

import com.github.cloudyrock.dimmer.*;

public class Main {

    public static void main(String[] args) {
//        DimmerServer.run(DimmerServerType.SPRING, args);
        runClient();
    }

    private static void runClient() {

        final DimmerConfigService dimmerConfigService = DimmerConfigService.builder()
                .connectionTimeoutMillis(1000)
                .readTimeoutMillis(5 * 1000)
                .getClient("http://localhost:8080");

        final DimmerConfigResponse dimmerConfigResponse1 = dimmerConfigService.getConfigByEnvironment("LOCAL");
//        final String dimmerConfigResponse2 = dimmerConfigService.getConfigByEnvironment("DEV");
//        final String dimmerConfigResponse3 = dimmerConfigService.getConfigByEnvironment("STAGING");
//        final String dimmerConfigResponse4 = dimmerConfigService.getConfigByEnvironment("PROD");
        System.out.println("OUT:" + dimmerConfigResponse1.toString());
//        System.out.println("OUT:" + dimmerConfigResponse2.toString());
//        System.out.println("OUT:" + dimmerConfigResponse3.toString());
//        System.out.println("OUT:" + dimmerConfigResponse4.toString());
    }


//    dimmer:
//    v1:
//      environments:
//          dev:
//              featureIntercept:
//                  - feature2
//                  - feature3
//                  - feature2
//                  - feature3
//                  - feature2
//                  - feature3
//          staging:
//    server: http://asdasd.com:880
//    prod:
//    server: http://asdasd.com:880

}
