package com.makeus.jfive.famo.src.main.today

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.src.presentation.today.MemoAdapter
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList

abstract class MemoSwipeHelper(adapter: MemoAdapter, context:Context, private val recyclerView:RecyclerView, internal var buttonWidth:Int, private val dragListener:(Int, Int)->Unit)
    :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT) {
    val dragAdapter = adapter
    private var buttonList:MutableList<SwipeButton> ?= null
    lateinit var gestureDetector:GestureDetector
    var swipePosition = -1
    var swipeThreshold = 0.5f
    val buttonBuffer:MutableMap<Int,MutableList<SwipeButton>> = mutableMapOf()
    lateinit var removeQueue:LinkedList<Int>

    abstract fun instantiateMyButton(viewHolder:RecyclerView.ViewHolder,buffer:MutableList<SwipeButton>)


    private val gestureListener = object:GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            for(button in buttonList!!){
                if(button.onClick(e!!.x,e!!.y))
                    break
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if(swipePosition < 0) return@OnTouchListener false
        val point =  Point(motionEvent.rawX.toInt(),motionEvent.rawY.toInt())
        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        try {
            val swipedItem = swipeViewHolder!!.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)

            if(motionEvent.action == MotionEvent.ACTION_DOWN ||
                    motionEvent.action == MotionEvent.ACTION_MOVE ||
                    motionEvent.action == MotionEvent.ACTION_UP){
                if(rect.top < point.y && rect.bottom > point.y)
                    gestureDetector.onTouchEvent(motionEvent)
                else {
                    removeQueue.add(swipePosition)
                    swipePosition = -1
                    recoverSwipeItem()
                }
            }
        }catch (e:NullPointerException){
            Log.d("TAG", "$e: ")
        }

        false
    }
    @Synchronized
    private fun recoverSwipeItem() {
        while(!removeQueue.isEmpty()) {
            val pos = removeQueue.poll()!!.toInt()
            if(pos > -1){
                recyclerView.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    init{
        this.buttonList = ArrayList()
        this.gestureDetector = GestureDetector(context,gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.removeQueue = IntLinkedList()
        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLinkedList : LinkedList<Int>() {
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if(contains(element))
                false
            else super.add(element)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        dragAdapter.swapItems(viewHolder.adapterPosition,target.adapterPosition)
        dragListener(viewHolder.adapterPosition,target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos= viewHolder.adapterPosition
        if(swipePosition != pos)removeQueue.add(swipePosition)
        swipePosition = pos
        if(buttonBuffer.containsKey(swipePosition))buttonList = buttonBuffer[swipePosition]
        else buttonList!!.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f * buttonList!!.size.toFloat() * buttonWidth.toFloat()
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if(pos<0){
            swipePosition = pos
            return
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            if(dX <0){
                var buffer : MutableList<SwipeButton> = ArrayList()
                if(!buttonBuffer.containsKey(pos)){
                    instantiateMyButton(viewHolder,buffer)
                    buttonBuffer[pos] = buffer
                }else{
                    buffer = buttonBuffer[pos]!!
                }
                translationX = dX * buffer.size.toFloat() * buttonWidth.toFloat()  / itemView.width
                drawButton(c,itemView,buffer,pos,translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<SwipeButton>, pos: Int, translationX: Float) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translationX/buffer.size
        var idx = 0
        for(button in buffer){
            val left = right - dButtonWidth
            if(idx == 0){
                button.onDraw(c, RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
                right = left
            }else{
                button.onDraw(c, RectF(left-75,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
                right = left

            }
            idx++
        }
    }

}