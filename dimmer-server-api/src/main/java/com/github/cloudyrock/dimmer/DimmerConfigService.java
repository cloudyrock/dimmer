package com.github.cloudyrock.dimmer;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import java.util.concurrent.TimeUnit;

import static com.github.cloudyrock.dimmer.ApiPaths.ENV_PATH;
import static com.github.cloudyrock.dimmer.ApiPaths.ROOT_PATH;

public interface DimmerConfigService {

//TODO test builder

    @Headers("Content-Type: application/json")
    @RequestLine("GET " + ROOT_PATH + ENV_PATH)
    DimmerConfigResponse getConfigByEnvironment(@Param("environment") String environment);

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private int connectionTimeout = 1000;
        private int readTimeout = 1000;
        private Encoder encoder;
        private Decoder decoder;
        private long period = 100L;
        private long maxPeriod = TimeUnit.SECONDS.toMillis(1L);
        private int maxAttempts = 3;



        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder connectionTimeoutMillis(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder readTimeoutMillis(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder encoder(Encoder encoder) {
            this.encoder = encoder;
            return this;
        }

        public Builder decoder(Decoder decoder) {
            this.decoder = decoder;
            return this;
        }


        public DimmerConfigService getClient(String host) {
            return Feign.builder()
                    .options(new Request.Options(connectionTimeout, readTimeout))
                    .encoder(encoder != null ? encoder : new JacksonEncoder())
                    .decoder(decoder != null ? decoder : new JacksonDecoder())
                    .retryer(buildRetryer())
                    .target(DimmerConfigService.class, host);

        }

        private Retryer buildRetryer() {
            return new Retryer.Default(period,  maxPeriod, maxAttempts);
        }

    }

}
