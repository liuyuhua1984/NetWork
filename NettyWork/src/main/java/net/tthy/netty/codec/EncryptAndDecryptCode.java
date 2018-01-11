package net.tthy.netty.codec;

/** 
 * ClassName:Encode <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2014-6-13 下午3:54:20 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
/**
 * ClassName: Encode <br/>
 * Function: TODO (加密). <br/>
 * Reason: TODO (). <br/>
 * date: 2014-6-13 下午3:54:20 <br/>
 * 
 * @author lyh
 * @version
 */
public class EncryptAndDecryptCode {
    
    /**
     * encrypt:(). <br/>
     * TODO().<br/>
     * 加密
     * 
     * @author lyh
     * @param data
     */
    public static byte[] encrypt(byte input[],int[]sign) {
        if (input == null || input.length < 1 || sign == null || sign.length < 3) {
            return null;
        }

        byte k = (byte)sign[0];
        int c1 = sign[1];
        int c2 = sign[2];
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte) (input[i] ^ k);
           // k = (input[i] + k) + (c1 + c2);
        }
        return input;
    }

    /**
     * dencrypt:(). <br/>
     * TODO().<br/>
     * 解密
     * 
     * @author lyh
     * @param input
     * @return
     */
    public static byte[] decrypt(byte input[],int[]sign) {
        if (input == null || input.length < 1 || sign == null || sign.length < 3) {
            return null;
        }

        byte k = (byte)sign[0];
        int c1 = sign[1];
        int c2 = sign[2];
        int previous = 0;
        for (int i = 0; i < input.length; i++) {
            previous = input[i];
          //  System.err.println("previous::"+previous);
            input[i] = (byte) (input[i] ^ k);
          //  System.err.println("input[i]:"+input[i]);
           // k = (previous + k) + (c1 + c2);
          //  System.err.println("k:"+k);
        }
        return input;
    }
}
