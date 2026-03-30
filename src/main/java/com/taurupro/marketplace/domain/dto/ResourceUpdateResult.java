package com.taurupro.marketplace.domain.dto;

import java.util.List;

public record ResourceUpdateResult(BullDto updated, List<String> oldKeys) {
}
