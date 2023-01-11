package com.softknife.release.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author alexander matsaylo on 2/24/22
 * @project devops-tools
 */
@ConfigurationProperties("security")
@Getter
@Setter
public class SecurityProp {

    private String user;
    private String password;

}
