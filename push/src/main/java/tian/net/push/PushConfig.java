package tian.net.push;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.Arrays;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/12 下午4:31<br/>
 */

public class PushConfig {

    public Object heatbeatEntity;

    public String host = "localhost";
    public int port = 8080;
    /**
     * 默认 4*60 = 240秒
     */
    public Heartbeat heartbeat = new Heartbeat();

    /**
     * 默认重连10次，间隔： 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 秒
     */
    public int[] retry = new int[]{2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public Heartbeat getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Heartbeat heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int[] getRetry() {
        return retry;
    }

    public void setRetry(int[] retry) {
        this.retry = retry;
    }

    public static class Heartbeat {
        public static final String MODE_NETTY = "netty";
        public static final String MODE_ALARM = "alarm";

        int step = 240;
        String mode = "netty";
        String ping = "1";
        String pong = "-1";

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getPing() {
            return ping;
        }

        public void setPing(String ping) {
            this.ping = ping;
        }

        public String getPong() {
            return pong;
        }

        public void setPong(String pong) {
            this.pong = pong;
        }


        @Override
        public String toString() {
            return "{" +
                    "step=" + step +
                    ", mode='" + mode + '\'' +
                    ", ping='" + ping + '\'' +
                    ", pong='" + pong + '\'' +
                    '}';
        }
    }

    public void setConfig(PushConfig config) {
        if(config == null) return;
        if(!TextUtils.isEmpty(config.getHost())) {
            this.setHost(config.getHost());
        }

        if(config.getPort() != 0) {
            this.setPort(config.getPort());
        }

        if(config.getRetry() != null && config.getRetry().length > 0) {
            config.setRetry(config.getRetry());
        }

        /*if(config.getHeartbeat() != 0) {
            this.setHeartbeat(config.getHeartbeat());
        }*/

        Heartbeat hb = config.getHeartbeat();
        if(hb != null) {
            if(hb.getStep() != 0) {
                this.getHeartbeat().setStep(hb.getStep());
            }
            if(!TextUtils.isEmpty(hb.getPing())) {
                this.getHeartbeat().setPing(hb.getPing());
            }
            if(!TextUtils.isEmpty(hb.getPong())) {
                this.getHeartbeat().setPong(hb.getPong());
            }
            if(!TextUtils.isEmpty(hb.getMode())) {
                if(Heartbeat.MODE_NETTY.equals(hb.getMode())) {
                    this.getHeartbeat().setMode(hb.getMode());
                }else {
                    this.getHeartbeat().setMode(Heartbeat.MODE_ALARM);
                }
            }
        }
    }

    public Object getHeatbeatEntity() {
        return heatbeatEntity;
    }

    public void setHeatbeatEntity(Object heatbeatEntity) {
        this.heatbeatEntity = heatbeatEntity;
    }

    @Override
    public String toString() {
        return "PushConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", heartbeat=" + heartbeat +
                ", retry=" + Arrays.toString(retry) +
                '}';
    }
}
