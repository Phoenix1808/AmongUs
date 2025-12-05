package com.example.uploadingscreen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.min

class ConnectDotActivity : AppCompatActivity() {

    private lateinit var gridView: ConnectGridView
    private lateinit var btnRestart: Button
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_dot)

        val container = findViewById<FrameLayout>(R.id.gridContainer)
        btnRestart = findViewById(R.id.btnRestart)
        tvStatus = findViewById(R.id.tvStatus)

        gridView = ConnectGridView(this)
        container.addView(
            gridView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        btnRestart.setOnClickListener {
            gridView.newGame()
            tvStatus.text = "Connect all colors!"
        }
    }


    inner class ConnectGridView(context: Context) : View(context) {

        private val GRID = 5

        private val grid = Array(GRID) { Array(GRID) { CellType.EMPTY } }
        private val occupied = Array(GRID) { Array(GRID) { false } }

        private val finalizedPaths = mutableMapOf<CellType, MutableList<Cell>>()
        private val endpoints = mutableMapOf<CellType, Pair<Cell, Cell>>()

        private var currentColor: CellType? = null
        private val currentPath = mutableListOf<Cell>()

        private var hasWon = false


        private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#222222")
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }

        private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }

        private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 56f
        }

        private val colorMap = mapOf(
            CellType.RED to Color.parseColor("#FF4B4B"),
            CellType.BLUE to Color.parseColor("#4BA6FF"),
            CellType.PURPLE to Color.parseColor("#B86BFF"),
            CellType.YELLOW to Color.parseColor("#FFD24D")
        )

        init {
            setupPattern()
        }

        private fun setupPattern() {

            for (r in 0 until GRID) for (c in 0 until GRID) {
                grid[r][c] = CellType.EMPTY
                occupied[r][c] = false
            }

            val pattern = mapOf(
                CellType.RED to Pair(Cell(0,0), Cell(0,4)),
                CellType.BLUE to Pair(Cell(4,0), Cell(4,4)),
                CellType.YELLOW to Pair(Cell(2,0), Cell(2,4)),
                CellType.PURPLE to Pair(Cell(1,1), Cell(3,1))
            )

            endpoints.clear()

            for ((color, pair) in pattern) {
                endpoints[color] = pair
                val (a,b) = pair
                grid[a.r][a.c] = color
                grid[b.r][b.c] = color
            }
        }

        fun newGame() {
            hasWon = false
            currentPath.clear()
            currentColor = null
            finalizedPaths.clear()

            setupPattern()
            invalidate()
        }


        private val boardLeft get() = paddingLeft.toFloat()
        private val boardTop get() = paddingTop.toFloat()
        private val boardSize get() = min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom).toFloat()
        private val cellSize get() = boardSize / GRID.toFloat()
        private val dotRadius get() = cellSize * 0.26f
        private val strokeWidth get() = cellSize * 0.48f

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.parseColor("#0B0B0D"))

            pathPaint.strokeWidth = strokeWidth

            drawGrid(canvas)
            drawFinalPaths(canvas)
            drawCurrentPath(canvas)
            drawDots(canvas)

            if (hasWon) drawWin(canvas)
        }

        private fun drawGrid(canvas: Canvas) {
            for (i in 0..GRID) {
                val x = boardLeft + i * cellSize
                val y = boardTop + i * cellSize
                canvas.drawLine(x, boardTop, x, boardTop + GRID * cellSize, gridPaint)
                canvas.drawLine(boardLeft, y, boardLeft + GRID * cellSize, y, gridPaint)
            }
        }

        private fun drawDots(canvas: Canvas) {
            for (r in 0 until GRID) for (c in 0 until GRID) {
                val type = grid[r][c]
                if (isColor(type)) {
                    dotPaint.color = colorMap[type]!!
                    val cx = boardLeft + c * cellSize + cellSize / 2
                    val cy = boardTop + r * cellSize + cellSize / 2
                    canvas.drawCircle(cx, cy, dotRadius, dotPaint)
                    canvas.drawCircle(cx, cy, dotRadius * 0.25f, Paint().apply { color = Color.WHITE })
                }
            }
        }

        private fun drawFinalPaths(canvas: Canvas) {
            for ((color, path) in finalizedPaths) {
                pathPaint.color = colorMap[color]!!
                for (i in 0 until path.size - 1) {
                    drawLine(canvas, path[i], path[i+1])
                }
            }
        }

        private fun drawCurrentPath(canvas: Canvas) {
            val color = currentColor ?: return
            if (currentPath.size < 2) return

            pathPaint.color = colorMap[color]!!
            for (i in 0 until currentPath.size - 1) {
                drawLine(canvas, currentPath[i], currentPath[i+1])
            }
        }

        private fun drawLine(canvas: Canvas, a: Cell, b: Cell) {
            val ax = boardLeft + a.c * cellSize + cellSize / 2
            val ay = boardTop + a.r * cellSize + cellSize / 2
            val bx = boardLeft + b.c * cellSize + cellSize / 2
            val by = boardTop + b.r * cellSize + cellSize / 2
            canvas.drawLine(ax, ay, bx, by, pathPaint)
        }

        private fun drawWin(canvas: Canvas) {
            val cx = width / 2f
            val cy = height / 2f
            canvas.drawText("You Win!", cx, cy, textPaint)
        }



        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (hasWon) return false

            val col = ((event.x - boardLeft) / cellSize).toInt().coerceIn(0, GRID-1)
            val row = ((event.y - boardTop) / cellSize).toInt().coerceIn(0, GRID-1)
            val cell = Cell(row, col)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    val type = grid[row][col]
                    if (isColor(type)) {
                        currentColor = type
                        currentPath.clear()
                        currentPath.add(cell)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    currentColor?.let { handleMove(cell, it) }
                }

                MotionEvent.ACTION_UP -> {
                    currentColor?.let { finishPath(it) }
                }
            }

            invalidate()
            return true
        }

        private fun handleMove(cell: Cell, color: CellType) {
            val last = currentPath.last()

            if (cell == last) return
            if (!isAdjacent(last, cell)) return

            currentPath.add(cell)
        }

        private fun finishPath(color: CellType) {
            val ep = endpoints[color]!!

            val first = currentPath.first()
            val last = currentPath.last()

            val valid = (first == ep.first && last == ep.second) ||
                    (first == ep.second && last == ep.first)

            if (valid) {
                finalizedPaths[color] = ArrayList(currentPath)
                checkWin()
            }

            currentPath.clear()
            currentColor = null
        }

        private fun checkWin() {
            val all = setOf(CellType.RED, CellType.BLUE, CellType.YELLOW, CellType.PURPLE)
            if (finalizedPaths.keys.containsAll(all)) {
                hasWon = true
                tvStatus.text = "You Win! 🎉"
            }
        }

        private fun isAdjacent(a: Cell, b: Cell) =
            abs(a.r - b.r) + abs(a.c - b.c) == 1

        private fun isColor(t: CellType) =
            t in listOf(CellType.RED, CellType.BLUE, CellType.YELLOW, CellType.PURPLE)
    }

    data class Cell(val r:Int, val c:Int)

    enum class CellType { EMPTY, RED, BLUE, YELLOW, PURPLE }
}
