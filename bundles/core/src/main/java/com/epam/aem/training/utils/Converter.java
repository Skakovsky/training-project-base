package com.epam.aem.training.utils;

import static com.epam.aem.training.beans.Constants.*;

public class Converter {
    public static String convertToEndPath(String resourcePath) {
        return HTTP_PROTOCOL + HOST + resourcePath + HTML_EXTENSION;
    }

}
