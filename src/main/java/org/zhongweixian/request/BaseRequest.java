package org.zhongweixian.request;

import org.hibernate.validator.constraints.NotBlank;
import org.zhongweixian.model.Channel;

import java.io.Serializable;

/**
 * Created by caoliang on 2018/8/22
 */
public class BaseRequest implements Serializable {
    /**
     * 支付平台
     **/
    @NotBlank(message = "支付平台不能为空")
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
