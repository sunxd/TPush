package tian.net.terminal.encrypt;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午6:33<br/>
 */

public interface Encryption {

    byte getEncryption();

    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);

}
