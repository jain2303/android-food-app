package com.example.foodapp




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderFragment : Fragment() {

    private lateinit var ordersRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        // Initialize the Firebase Realtime Database reference for "orders" collection
        ordersRef = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .child("orders")

        // Get a reference to the RecyclerView in your fragment's layout
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set up the RecyclerView with the Adapter
        ordersAdapter = OrdersAdapter(emptyList())
        recyclerView.adapter = ordersAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Read data from the Firebase Realtime Database
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orderList = mutableListOf<Breakfast>()

                for (orderSnapshot in dataSnapshot.children) {
                    // Retrieve data for each order and extract the desired fields
                    val itemName = orderSnapshot.child("itemName").getValue(String::class.java) ?: ""
                    val itemPrice = orderSnapshot.child("itemPrice").getValue(Double::class.java) ?: 0.0
                    val itemDescription = orderSnapshot.child("itemDescription").getValue(String::class.java) ?: ""
                    val itemImageUrl = orderSnapshot.child("itemImageUrl").getValue(String::class.java) ?: ""

                    val order = Breakfast(itemName, itemPrice, itemDescription, itemImageUrl)
                    orderList.add(order)
                }

                // Update the RecyclerView with the new order data
                ordersAdapter.updateData(orderList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        }

        ordersRef.addValueEventListener(valueEventListener)

        return view
    }
}