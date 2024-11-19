package com.titans.ahs.service.llama;

import com.titans.ahs.model.llama.LlamaResponse;

public interface LlamaAiService {

    LlamaResponse generateMessage(String prompt);
    LlamaResponse generateJoke(String topic);
}
