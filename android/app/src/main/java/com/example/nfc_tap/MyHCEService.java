import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class MyHCEService extends HostApduService {
    private static final String TAG = "MyHCEService";

    // APDU (Application Protocol Data Unit) untuk SELECT AID
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};
    private static final byte[] UNKNOWN_CMD_SW = {(byte) 0x00, (byte) 0x00};
    private static final String RESPONSE_STRING = "AuthSuccess";

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG, "Received APDU: " + bytesToHex(commandApdu));

        // Contoh respons ketika menerima SELECT AID
        if (isSelectAidApdu(commandApdu)) {
            return concatByteArrays(RESPONSE_STRING.getBytes(), SELECT_OK_SW);
        }

        return UNKNOWN_CMD_SW;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "HCE service deactivated: " + reason);
    }

    // Cek apakah APDU yang diterima adalah SELECT AID
    private boolean isSelectAidApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte) 0x00 && apdu[1] == (byte) 0xA4;
    }

    private byte[] concatByteArrays(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
