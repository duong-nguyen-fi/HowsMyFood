package com.beuwolf.howsmyfood

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import co.metalab.asyncawait.async
import kotlinx.android.synthetic.main.item.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Beuwolf on 18-Nov-17.
 */
 open class RVAdapter : RecyclerView.Adapter<RVAdapter.FoodViewHolder> {



    open  class FoodViewHolder : RecyclerView.ViewHolder {

        lateinit var cv : CardView
        lateinit var foodName : TextView
        lateinit var foodDate : TextView
        lateinit var foodPhoto : ImageView

         constructor(itemView: View) : super(itemView)
         {
            // Log.i("myInfo","FoodViewHolder created")
             cv = itemView.cv
             foodName = itemView.food_name
             foodDate = itemView.food_date
             foodPhoto = itemView.food_photo
         }


    } // end of FoodViewHolder class

    lateinit var foods : List<Food>
    lateinit var context: Context
    lateinit var sharedPreferences : SharedPreferences
    constructor(foods: List<Food>, context : Context)
    {
        this.foods = foods
        this.context = context
       // Log.i("myInfo","Adapter Constructor loaded")
        sharedPreferences = context.getSharedPreferences("com.beuwolf.howsmyfood", android.content.Context.MODE_PRIVATE )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        //Log.i("myInfo","onAttachRV created")
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
           // Log.i("myInfo","ViewHolder created")
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            val fvh = FoodViewHolder(v)
            return fvh


    }

    override fun onBindViewHolder(holder: FoodViewHolder?, position: Int) {
        if (holder != null) {
            //Log.i("myInfo","FoodViewHolder is not null")
            holder.foodName.setText(foods.get(position).name)
            holder.foodDate.setText(foods.get(position).date)
            holder.foodPhoto.setImageURI(foods.get(position).photoURI)

            holder.itemView.setOnClickListener(View.OnClickListener{view: View ->
                run({


                    async{
                        var file : File = File(foods.get(position).photoURI.path)

                        Log.i("myInfo ", file.absoluteFile.absolutePath)

                        foods -= foods.get(position)

                        var mfoods = ArrayList(foods)
                        sharedPreferences.edit().putString("foodShared", ObjectSerializer.serialize(mfoods)).apply()
                       // Log.i("myInfo", "foods.size when remove = " + foods.size)

                        file.delete()

                        if (!file.exists())
                            Log.i("myInfo", "File deleted")
                        notifyDataSetChanged()
                    }

                })
            })


        }
        else{
            //Log.i("myInfo","ViewHolder is null")
        }
    }

    override fun getItemCount(): Int {
        return (foods.size)
    }


}