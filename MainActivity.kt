import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var hourlyRateInput: EditText
    private lateinit var startOverlayButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hourlyRateInput = findViewById(R.id.hourlyRateInput)
        startOverlayButton = findViewById(R.id.startOverlayButton)

        startOverlayButton.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService()
            } else {
                requestOverlayPermission()
            }
        }
    }

    private fun startOverlayService() {
        val hourlyRate = hourlyRateInput.text.toString().toFloatOrNull() ?: 0f
        val intent = Intent(this, OverlayService::class.java).apply {
            putExtra("HOURLY_RATE", hourlyRate)
        }
        startService(intent)
    }

    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService()
            }
        }
    }

    companion object {
        private const val OVERLAY_PERMISSION_REQUEST_CODE = 1
    }
}