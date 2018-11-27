package id.rackspira.seedisaster.ui.buatposko

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import id.rackspira.seedisaster.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import id.rackspira.seedisaster.data.network.entity.DataUser
import id.rackspira.seedisaster.data.network.entity.ListBencana
import kotlinx.android.synthetic.main.activity_buat_posko.*


class BuatPoskoActivity : AppCompatActivity(), BuatPoskoView {

    private lateinit var presenter: BuatPoskoPresenter
    private lateinit var mAuth: FirebaseAuth
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var telefon: String? = null
    private var namaPenangungjawab: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_posko)
        presenter = BuatPoskoPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        presenter.getUser()

        val dataPosko = intent.getParcelableExtra<ListBencana>("data")
        Log.d("idbencana", dataPosko.kib)
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location?.latitude
                    longitude = location?.longitude
                }
        }
        backBuatPosko.setOnClickListener {
            finish()
        }

        buttonSimpan.setOnClickListener {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Mohon Hidupkan Lokasi Anda", Toast.LENGTH_LONG).show()
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        latitude = location?.latitude
                        longitude = location?.longitude

                        if (latitude.toString().isEmpty()) run { Log.d("lokasi","lokasi") }
                        else if (latitude.toString().isEmpty()) run { Log.d("lokasi","lokasi")}
                        else if (edittextDesa.text.toString().isEmpty()) run { edittextDesa.error = "Data Kosong" }
                        else if (edittextKec.text.toString().isEmpty()) run { edittextKec.error = "Data kosong" }
                        else if (textviewKab.text.toString().isEmpty()) run { textviewKab.error = "Data kosong" }
                        else if (edittextNamaPosko.text.toString().isEmpty()) run { edittextNamaPosko.error = "Data kosong" }
                        else if (editTextProv.text.toString().isEmpty()) run { editTextProv.error = "Data kosong" }
                        else{
                            presenter.tambahPosko(
                                dataPosko.kib.toString(), mAuth.currentUser!!.uid, edittextNamaPosko.text.toString(),
                                latitude.toString(), longitude.toString(),telefon.toString(), edittextDesa.text.toString(), edittextKec.text.toString(), textviewKab.text.toString(), editTextProv.text.toString(), namaPenangungjawab.toString()
                            )

                        }

                    }
            } else {
                ActivityCompat.requestPermissions(this, permissions, 0)
            }
        }

    }

    override fun onSuccess(msg: String?) {

    }

    override fun onFailed(ms: String?) {}

    override fun getDataUser(dataUser: DataUser) {
        Log.d("telefonAnda", dataUser.noTp.toString())
        telefon = dataUser.noTp.toString()
        namaPenangungjawab = dataUser.nama.toString()


    }
}
