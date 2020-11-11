package com.padc.demo.core.Auditing;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImplementation  implements AuditorAware<String>
{
    @Override
    public Optional<String> getCurrentAuditor()
    {
        return Optional.of("Admin");
    }
}
