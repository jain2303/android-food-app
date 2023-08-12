package com.example.foodapp

import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class StartersActivity : AppCompatActivity(), BreakfastAdapter.OrderClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var breakfastAdapter: BreakfastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breakfast)

        // Initialize the Firebase Realtime Database reference for "shirts" category
        database = FirebaseDatabase.getInstance().reference.child("food_item").child("starter")

        // Get a reference to the RecyclerView in your activity's layout
        recyclerView = findViewById(R.id.recyclerView)

        // Set up the RecyclerView with the Adapter and pass `this` as the click listener
        breakfastAdapter = BreakfastAdapter(emptyList(),  this)
        recyclerView.adapter = breakfastAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Read data from the Firebase Realtime Database
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val breakfastList = mutableListOf<Breakfast>()

                for (itemSnapshot in dataSnapshot.children) {
                    val name = itemSnapshot.child("name").getValue(String::class.java) ?: ""
                    val price = itemSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                    val description = itemSnapshot.child("description").getValue(String::class.java) ?: ""
                    val imageUrl = itemSnapshot.child("image_url").getValue(String::class.java) ?: ""

                    val breakfast = Breakfast(name, price, description, imageUrl)
                    breakfastList.add(breakfast)
                }

                // Update the RecyclerView with the new shirt data
                breakfastAdapter.updateData(breakfastList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        }

        database.addValueEventListener(valueEventListener)
    }


    override fun onOrderClick(breakfast: Breakfast) {
        showOrderForm(breakfast)
    }




    private fun showOrderForm(breakfast: Breakfast) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.order_form_layout)

        // Get references to the views in the order form
        val fullNameEditText = dialog.findViewById<EditText>(R.id.fullNameEditText)
        val mobileNumberEditText = dialog.findViewById<EditText>(R.id.mobileNumberEditText)
        val emailEditText = dialog.findViewById<EditText>(R.id.emailEditText)
        val addressEditText = dialog.findViewById<EditText>(R.id.addressEditText)
        val itemNameTextView = dialog.findViewById<TextView>(R.id.itemNameTextView)
        val itemPriceTextView = dialog.findViewById<TextView>(R.id.itemPriceTextView)
        val itemDescriptionTextView = dialog.findViewById<TextView>(R.id.itemDescriptionTextView)
        val itemImageView = dialog.findViewById<ImageView>(R.id.imageview1)

        // Populate the order form fields with the shirt details
        itemNameTextView.text = breakfast.name
        itemPriceTextView.text = "Price: $${breakfast.price}"
        itemDescriptionTextView.text = breakfast.description

        Picasso.get().load(breakfast.imageUrl).into(itemImageView)

        // Set a click listener for the "Order" button in the order form
        val orderButton = dialog.findViewById<Button>(R.id.orderButton)
        orderButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val mobileNumber = mobileNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()

            if (fullName.isNotEmpty() && mobileNumber.isNotEmpty() && email.isNotEmpty() && address.isNotEmpty()) {
                // Valid form data, create an order in the database
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val orderRef = FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(currentUser.uid)
                        .child("orders")
                        .push()

                    val orderData = HashMap<String, Any>()
                    orderData["fullName"] = fullName
                    orderData["mobileNumber"] = mobileNumber
                    orderData["email"] = email
                    orderData["address"] = address
                    orderData["itemName"] = breakfast.name // Add the shirt name to the order data
                    orderData["itemPrice"] = breakfast.price // Add the shirt price to the order data
                    orderData["itemDescription"] = breakfast.description // Add the shirt description to the order data
                    orderData["itemImageUrl"] = breakfast.imageUrl

                    orderRef.updateChildren(orderData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // User not logged in, handle this case if necessary
                    Toast.makeText(this, "Please log in to place an order.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}