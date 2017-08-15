package com.waracle.androidtest.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();

    // Can you see what's wrong with this???
    public static byte[] readUnknownFully( InputStream stream ) throws IOException {
        /**
         * Yes. This is being added to an ArrayList and then that list is converted to an array.
         * This can be done in many different ways, for example reading into some kind of buffer
         * and then returning the content of that buffer.
         *
         * There could also be issue with lack of memory.
         */

        // Read in stream of bytes
        ArrayList<Byte> data = new ArrayList<>();
        while( true ) {
            int result = stream.read();
            if( result == -1 ) {
                break;
            }
            data.add((byte) result);
        }

        // Convert ArrayList<Byte> to byte[]
        byte[] bytes = new byte[data.size()];
        for( int i = 0; i < bytes.length; i++ ) {
            bytes[i] = data.get(i);
        }

        // Return the raw byte array.
        return bytes;
    }

    public static void close( Closeable closeable ) {
        if( closeable != null ) {
            try {
                closeable.close();
            }
            catch( IOException e ) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
