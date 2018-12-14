package util;

@org.aeonbits.owner.Config.Sources("classpath:build.properties")
public interface Config extends org.aeonbits.owner.Config{
    @Key("host")
    String host();

    @Key("port")
    Integer port();
}