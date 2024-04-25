package wjh.projects.interfaces.config;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    SUCCESS("200", "请求成功"),
    ESTIMATE_WAIT("204", "未达到预计到计算间隔"),
    METHOD_ARGUMENT_NOT_VALID("400", "非法参数"),
    RESOURCE_NOT_FOUND("404", "目标资源不存在");
    private final String code;
    private final String message;

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
