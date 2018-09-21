/*
Copyright 2018 FZI Forschungszentrum Informatik

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.streampipes.connect.adapter.generic.protocol.stream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;
import org.streampipes.connect.adapter.generic.format.Format;
import org.streampipes.connect.adapter.generic.format.Parser;
import org.streampipes.connect.adapter.generic.guess.SchemaGuesser;
import org.streampipes.connect.adapter.generic.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streampipes.connect.adapter.generic.sdk.ParameterExtractor;
import org.streampipes.connect.exception.AdapterException;
import org.streampipes.model.connect.guess.GuessSchema;
import org.streampipes.model.connect.grounding.ProtocolDescription;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.staticproperty.FreeTextStaticProperty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpStreamProtocol extends PullProtocoll {

    Logger logger = LoggerFactory.getLogger(HttpStreamProtocol.class);

    public static final String ID = "https://streampipes.org/vocabulary/v1/protocol/stream/http";
    private static String URL_PROPERTY ="url";
    private static String INTERVAL_PROPERTY ="interval";
    private static String ACCESS_TOKEN_PROPERTY ="access_token";

    private String url;
    private String accessToken;

    public HttpStreamProtocol() {
    }



    public HttpStreamProtocol(Parser parser, Format format, String url, long interval, String accessToken) {
        super(parser, format, interval);
        this.url = url;
        this.accessToken = accessToken;
    }

    @Override
    public Protocol getInstance(ProtocolDescription protocolDescription, Parser parser, Format format) {
        ParameterExtractor extractor = new ParameterExtractor(protocolDescription.getConfig());

        String urlProperty = extractor.singleValue(URL_PROPERTY);
        try {
            long intervalProperty = Long.parseLong(extractor.singleValue(INTERVAL_PROPERTY));
            String accessToken = extractor.singleValue(ACCESS_TOKEN_PROPERTY);
            return new HttpStreamProtocol(parser, format, urlProperty, intervalProperty, accessToken);
        } catch (NumberFormatException e) {
            logger.error("Could not parse" + extractor.singleValue(INTERVAL_PROPERTY) + "to int");
            return null;
        }

    }

    @Override
    public ProtocolDescription declareModel() {
        ProtocolDescription description = new ProtocolDescription(ID, "HTTP Stream", "This is the " +
                "description for the http stream protocol");

        FreeTextStaticProperty urlProperty = new FreeTextStaticProperty(URL_PROPERTY, "URL", "This property " +
                "defines the URL for the http request.");


        FreeTextStaticProperty intervalProperty = new FreeTextStaticProperty(INTERVAL_PROPERTY, "Interval", "This property " +
                "defines the pull interval in seconds.");

        FreeTextStaticProperty accessToken = new FreeTextStaticProperty(ACCESS_TOKEN_PROPERTY, "Access Token", "Http Access Token");

        description.setSourceType("STREAM");
        description.addConfig(urlProperty);
        description.addConfig(intervalProperty);
        description.addConfig(accessToken);

        return description;
    }

    @Override
    public GuessSchema getGuessSchema() {
        int n = 1;

        InputStream dataInputStream = getDataFromEndpoint();

        List<byte[]> dataByte = parser.parseNEvents(dataInputStream, n);
        if (dataByte.size() < n) {
            logger.error("Error in HttpStreamProtocol! Required: " + n + " elements but the resource just had: " +
                    dataByte.size());

            dataByte.addAll(dataByte);
        }
        EventSchema eventSchema= parser.getEventSchema(dataByte);
        GuessSchema result = SchemaGuesser.guessSchma(eventSchema, getNElements(n));

        return result;
    }

    @Override
    public List<Map<String, Object>> getNElements(int n) {
        List<Map<String, Object>> result = new ArrayList<>();

         InputStream   dataInputStream = getDataFromEndpoint();

        List<byte[]> dataByte = parser.parseNEvents(dataInputStream, n);

        // Check that result size is n. Currently just an error is logged. Maybe change to an exception
        if (dataByte.size() < n) {
            logger.error("Error in HttpStreamProtocol! User required: " + n + " elements but the resource just had: " +
                    dataByte.size());
        }

        for (byte[] b : dataByte) {
            result.add(format.parse(b));
        }

        return result;
    }


    @Override
    public String getId() {
        return ID;
    }

    @Override
    InputStream getDataFromEndpoint() {
        InputStream result = null;

        try {
            Request request = Request.Get(url)
                    .connectTimeout(1000)
                    .socketTimeout(100000);

            if (this.accessToken != null && !this.accessToken.equals("")) {
                request.setHeader("Authorization", "Bearer " + this.accessToken);
            }

            String s = request
                    .execute().returnContent().asString();

            if (s.startsWith("ï")) {
                s = s.substring(3);
            }

            result = IOUtils.toInputStream(s, "UTF-8");

        } catch (Exception e) {
            logger.error("Error while fetching data from URL: " + url, e);

//            throw new AdapterException();
        }

        return result;
    }
}
