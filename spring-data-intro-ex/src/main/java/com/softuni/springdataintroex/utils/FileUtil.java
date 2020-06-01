package com.softuni.springdataintroex.utils;

import java.io.IOException;
import java.util.List;

public interface FileUtil {
    List<String> readFileContent(String filePath) throws IOException;
}
