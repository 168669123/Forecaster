package wjh.projects.interfaces.config;

import lombok.Getter;
import lombok.Setter;

/**
 * web 统一响应消息
 */
@Setter
@Getter
public class ResponseMessage {
    private String code;
    private String message;
    private Object data;

    public ResponseMessage(ResponseCodeEnum responseCodeEnum, Object data) {
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getMessage();
        this.data = data;
    }
}
