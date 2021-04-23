package com.tang.platform.id.generator.service.converter;

import com.tang.platform.id.generator.service.Id;

public interface IdConverter {
    long convert(Id id);
    Id reConvert(long id);
}
