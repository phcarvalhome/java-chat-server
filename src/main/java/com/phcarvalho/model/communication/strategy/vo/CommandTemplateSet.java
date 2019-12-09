package com.phcarvalho.model.communication.strategy.vo;

import com.phcarvalho.model.communication.commandtemplate.IChatCommandTemplate;

public class CommandTemplateSet {

    private IChatCommandTemplate chatCommandTemplate;

    public CommandTemplateSet(IChatCommandTemplate chatCommandTemplate) {
        this.chatCommandTemplate = chatCommandTemplate;
    }

    public IChatCommandTemplate getChatCommandTemplate() {
        return chatCommandTemplate;
    }
}
