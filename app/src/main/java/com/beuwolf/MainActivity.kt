package com.beuwolf.howsmyfood

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.View
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    //lateinit var rv : RecyclerView
    lateinit var adapter : RVAdapter
    lateinit  var  sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences("com.beuwolf.howsmyfood", android.content.Context.MODE_PRIVATE )
        setContentView(R.layout.activity_main)
        rv.setHasFixedSize(true)
        var llm : LinearLayoutManager = LinearLayoutManager(this)
        rv.layoutManager = llm

        //initializeData()
        initializeAdapter()

        add_button.setOnClickListener { view : View -> dispatchTakePhotoIntent() }

    }

    //decide directory/ file name using datetime stamp -> unique name

    lateinit var mCurrentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */

        )
        Log.i("myInfo- storageDir", storageDir.absolutePath)

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    val REQUEST_TAKE_PHOTO = 1
    var photoFile : File? = null
    lateinit var photoUri : Uri
    fun dispatchTakePhotoIntent()
    {
        val takePhotoIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.i("myInfo","Intent initialize")
        if(takePhotoIntent.resolveActivity(packageManager) != null)
        {

            try
            {
                photoFile = createImageFile()
                Log.i("myInfo","File Created")
            }
            catch (e : IOException)
            {
                Log.i("myInfo", "Cannot create file")
            }

            if (photoFile != null)
            {
                Log.i("myInfo","photoFile not null")
                photoUri = FileProvider.getUriForFile(this,"com.android.fileprovider",photoFile)
                Log.i("myInfo","photoURi OK")
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                Log.i("myInfo","Put Extra OK")
                startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO)
                Log.i("myInfo","Intent start OK")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.i("myInfo","Intent result")
        if (requestCode==REQUEST_TAKE_PHOTO)
        if (resultCode == Activity.RESULT_OK)
        {
            initializeData()
            //Log.i("myInfo","result OK")
            foods += Food("new Food" + foods.size, "2017-11-19", photoUri.toString())
            updateSharedPref()
        }
        initializeAdapter()
    }

    open fun updateSharedPref()
    {
        Log.i("myInfo", "foods size:" + foods.size)
        mfoods = ArrayList(foods)
        Log.i("myInfo", "cast from food to mfoods success")


        sharedPreferences.edit().putString("foodShared",ObjectSerializer.serialize(mfoods)).apply()
        Log.i("myInfo", "put sharedPre success")
    }



    lateinit var foods : List<Food>
    lateinit var mfoods : ArrayList<Food>
    fun initializeData()
    {

        foods = ArrayList<Food>()
//        val burgerStr = "android.resource://com.beuwolf.howsmyfood/drawable/burger"
//        val pizzaStr = "android.resource://com.beuwolf.howsmyfood/drawable/pizza"
//        val kebabStr = "android.resource://com.beuwolf.howsmyfood/drawable/kebab"
//
//        val burgerURI = Uri.parse(burgerStr)
//        val pizzaURI = Uri.parse(pizzaStr)
//        val kebabURI = Uri.parse(kebabStr)

        //Log.i("URI", kebabURI.toString())
        //Log.i("URI, Str", kebabStr)

        try {
            Log.i("myInfo", "before getSharedPr")
            var deserializedObject = ObjectSerializer.deserialize(sharedPreferences.getString("foodShared",  ObjectSerializer.serialize(ArrayList<String>() ) ) )
            if (deserializedObject != null) {
                mfoods = deserializedObject as ArrayList<Food>
                Log.i("myInfo", "foods.size = " + mfoods.size)
                foods = mfoods
                Log.i("myInfo", "foods.size = " + foods.size)
            }
        }
        catch (e : Exception)
        {
            Log.i("myError", e.toString())
        }


//        foods += Food("Burger", "2017-11-13", burgerURI)
//
//        foods += Food("Pizza", "2017-11-14", pizzaURI)
//        foods += Food("Kebab", "2017-11-15", kebabURI)
//        Log.i("myInfo","Food creation," + foods.size)


    }


    fun initializeAdapter()
    {

        initializeData()
        adapter = RVAdapter(foods, context = applicationContext)
        Log.i("myInfo","RVAPdapter created")
        rv.adapter = adapter

    }
}

