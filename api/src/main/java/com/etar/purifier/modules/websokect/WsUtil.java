package com.etar.purifier.modules.websokect;

import com.etar.purifier.modules.websokect.entity.WSResponeVo;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

/**
 * @author hzh
 * @date 2018/10/26
 */
public class WsUtil implements Encoder.Binary<WSResponeVo> {
    @Override
    public ByteBuffer encode(WSResponeVo wsResponeVo) throws EncodeException {
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
