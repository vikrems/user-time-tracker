package com.hidglobal.reports.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JsonNodeParser {

    private final ObjectMapper objectMapper;

    public JsonNode findValueAtNode(String nodeKey,
                                    String jsonString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonString);
        return searchRecursively(nodeKey, rootNode);
    }

    private JsonNode searchRecursively(String nodeKey, JsonNode node) {
        if (node instanceof ArrayNode) {
            return iterateNodesInArray(nodeKey, node);
        }

        Iterator<Map.Entry<String, JsonNode>> nodes = node.fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            JsonNode value;
            if (entry.getKey().equals(nodeKey)) {
                return entry.getValue();
            }
            if (entry.getValue() instanceof ObjectNode) {
                value = searchRecursively(nodeKey, entry.getValue());
                if (nonNull(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    private JsonNode iterateNodesInArray(String nodeKey, JsonNode node) {
        JsonNode result;
        for (JsonNode eachNode : node) {
            result = searchRecursively(nodeKey, eachNode);
            if (nonNull(result)) {
                return result;
            }
        }
        return null;
    }

    public static String extractValue(JsonNode attributes, String text) {
        return attributes.get(text).asText();
    }
}
