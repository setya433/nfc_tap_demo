package com.example.nfc_tap;

import android.nfc.cardemulation.HostApduService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class MyHostApduService extends HostApduService {
    private static final String TAG = "MyHostApduService";
    private static final byte[] AID_ANDROID = {(byte) 0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    private static final byte[] RESPONSE_ERROR = {(byte) 0x6A, (byte) 0x82};
    private static String memberId = "LOADING"; // Default sebelum API merespon

    @Override
    public void onCreate() {
        super.onCreate();
        fetchMemberId(); // Ambil member_id saat service pertama kali berjalan
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG, "Received APDU: " + Arrays.toString(commandApdu));

        if (commandApdu == null || commandApdu.length < 2) {
            return RESPONSE_ERROR;
        }

        // Cek apakah APDU adalah SELECT AID
        if (commandApdu[0] == (byte) 0x00 && commandApdu[1] == (byte) 0xA4) {
            Log.d(TAG, "SELECT AID detected!");
            if (Arrays.equals(Arrays.copyOfRange(commandApdu, 5, commandApdu.length), AID_ANDROID)) {
                Log.d(TAG, "AID cocok, mengirim member_id dari API");

                // Ubah memberId menjadi bytes dan kirim
                byte[] responseBytes = memberId.getBytes();
                byte[] responseWithOK = new byte[responseBytes.length + 2];

                System.arraycopy(responseBytes, 0, responseWithOK, 0, responseBytes.length);
                responseWithOK[responseBytes.length] = (byte) 0x90;
                responseWithOK[responseBytes.length + 1] = (byte) 0x00;

                return responseWithOK;
            } else {
                Log.d(TAG, "AID tidak cocok!");
                return RESPONSE_ERROR;
            }
        }

        Log.d(TAG, "APDU tidak dikenal");
        return RESPONSE_ERROR;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "HCE service deactivated, reason: " + reason);
    }

    private void fetchMemberId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String token = "YOUT_API_TOKEN_HERE";
                    URL url = new URL("YOUR_API_URL_HERE");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Bearer " + token);
                    conn.setRequestProperty("Content-Type", "application/json");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse JSON untuk mendapatkan member_id
                    String jsonResponse = response.toString();
                    return jsonResponse.split("\"member_id\":\"")[1].split("\"")[0];
                } catch (Exception e) {
                    return "ERROR"; // Jika gagal mengambil member_id
                }
            }

            @Override
            protected void onPostExecute(String result) {
                memberId = result;
                Log.d(TAG, "Member ID dari API: " + memberId);
            }
        }.execute();
    }
}
