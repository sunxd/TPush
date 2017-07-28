package tian.net.terminal.encrypt;

import tian.net.terminal.common.Config;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午6:44<br/>
 */

public class Encrypted implements Encryption {


    private static Encryption encryption;

    private Encrypted() {
    }

    public static Encrypted getInstance(Config.EncryptionType type) {
        Encrypted en = new Encrypted();
        //TODO
        if(type.equals(Config.EncryptionType.RSA)) {
            encryption = new Rsa();
        }else if(type.equals(Config.EncryptionType.AES)){
            encryption = new Aes();
        }else {
            encryption = new Normal();
        }
        return en;
    }

    @Override
    public byte getEncryption() {
        return encryption.getEncryption();
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return encryption.encrypt(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return encryption.decrypt(data);
    }

    public byte checkBCC(byte[] data) {
        byte b = data[0];
        for(int i=1; i<data.length; i++) {
            b = (byte)(b^data[i]);
        }
        return b;
    }

    static class Normal implements Encryption{

        @Override
        public byte getEncryption() {
            return 0x01;
        }

        @Override
        public byte[] encrypt(byte[] data) {
            return data;
        }

        @Override
        public byte[] decrypt(byte[] data) {
            return data;
        }

    }


    static class Rsa implements Encryption{

        @Override
        public byte getEncryption() {
            return 0x02;
        }

        @Override
        public byte[] encrypt(byte[] data) {
            return data;
        }

        @Override
        public byte[] decrypt(byte[] data) {
            return data;
        }
    }

    static class Aes implements Encryption{

        @Override
        public byte getEncryption() {
            return 0x03;
        }

        @Override
        public byte[] encrypt(byte[] data) {
            return data;
        }

        @Override
        public byte[] decrypt(byte[] data) {
            return data;
        }
    }
}
