package com.beuwolf.howsmyfood

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.cardview.*

/**
 * Created by Beuwolf on 18-Nov-17.
 */
class CardView_Activity : Activity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cardview)

        food_name.setText("Burger")
        food_date.setText("2017-11-13")
        food_photo.setImageResource(R.drawable.burger)
    }
}