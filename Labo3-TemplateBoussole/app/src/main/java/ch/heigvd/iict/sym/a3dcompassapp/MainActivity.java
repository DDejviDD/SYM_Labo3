/*
 * File     : MainActivity.java
 * Project  : TemplateBoussole
 * Author   : FRUEH Loïc
 * 			  KOUBAA Walid
 * 			  MUAREMI DEJVID
 *
 *
 * This piece of code reads is used for the main menu and link every view of the application.
 *
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // GUI elements
    private Button buttObjBAR       = null;
    private Button buttObjIBeacon   = null;
    private Button buttObjCompass   = null;
    private Button buttObjNFC       = null;
    private Button buttObjQR        = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Link GUI elements
        buttObjBAR      = findViewById(R.id.main_butt_bar);
        buttObjIBeacon  = findViewById(R.id.main_butt_beacon);
        buttObjCompass  = findViewById(R.id.main_butt_compass);
        buttObjNFC      = findViewById(R.id.main_butt_nfc);
        buttObjQR       = findViewById(R.id.main_butt_qr);

        // Give buttons an action
        buttObjBAR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast indicatif + Creation d'un Intent afin de start une nouvelle activité
                Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, BarActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        buttObjIBeacon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast indicatif + Creation d'un Intent afin de start une nouvelle activité
                Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, BeaconActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        buttObjCompass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast indicatif + Creation d'un Intent afin de start une nouvelle activité
                Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        buttObjNFC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast indicatif + Creation d'un Intent afin de start une nouvelle activité
                Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, NFCActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        buttObjQR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast indicatif + Creation d'un Intent afin de start une nouvelle activité
                Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, QRActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
