package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Scope with custom properties ofr a DataTable
 */
class DataTableScope(
    private val columnScope: ColumnScope,
    var borderStroke: BorderStroke,
    var contentAlignment: Alignment,
    var cellPaddingValues: PaddingValues,
) {
    @Composable
    operator fun invoke() {
    }

    companion object {
        /**
         * Scope with custom properties for a DataTable's TableRow
         */
        class TableRowScope(
            private val rowScope: RowScope,
            private val dataTableScope: DataTableScope,
        ) {
            var borderStroke: BorderStroke = this.dataTableScope.borderStroke
            var contentAlignment: Alignment = this.dataTableScope.contentAlignment
            var cellPaddingValues: PaddingValues = this.dataTableScope.cellPaddingValues

            @Composable
            operator fun invoke() {
            }

            @Composable
            fun Cell(
                modifier: Modifier = Modifier,
                borderStroke: BorderStroke = this.borderStroke,
                contentAlignment: Alignment = this.contentAlignment,
                cellPaddingValues: PaddingValues = this.cellPaddingValues,
                content: @Composable () -> Unit
            ) {
                rowScope.apply {
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .border(borderStroke),
                        contentAlignment = contentAlignment
                    ) {
                        Box(modifier.padding(cellPaddingValues)) {
                            content.invoke()
                        }
                    }
                }
            }

            @Composable
            fun ColorCell(
                modifier: Modifier = Modifier,
                borderStroke: BorderStroke = this.borderStroke,
                contentAlignment: Alignment = this.contentAlignment,
                cellPaddingValues: PaddingValues = this.cellPaddingValues,
                backgroundColor: Color,
                foregroundColor: Color,
                content: @Composable () -> Unit
            ) = Cell(
                modifier.background(backgroundColor),
                borderStroke,
                contentAlignment,
                cellPaddingValues
            ) {
                CompositionLocalProvider(LocalContentColor provides foregroundColor) {
                    content.invoke()
                }
            }

            @Composable
            fun HeadingCell(
                modifier: Modifier = Modifier,
                borderStroke: BorderStroke = this.borderStroke,
                contentAlignment: Alignment = this.contentAlignment,
                cellPaddingValues: PaddingValues = this.cellPaddingValues,
                backgroundColor: Color = MaterialTheme.colorScheme.primary,
                foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
                content: @Composable () -> Unit
            ) = ColorCell(
                modifier,
                borderStroke,
                contentAlignment,
                cellPaddingValues,
                backgroundColor,
                foregroundColor,
                content
            )

            @Composable
            fun SubheadingCell(
                modifier: Modifier = Modifier,
                borderStroke: BorderStroke = this.borderStroke,
                contentAlignment: Alignment = this.contentAlignment,
                cellPaddingValues: PaddingValues = this.cellPaddingValues,
                backgroundColor: Color = MaterialTheme.colorScheme.primary,
                foregroundColor: Color = MaterialTheme.colorScheme.onBackground,
                content: @Composable () -> Unit
            ) = ColorCell(
                modifier,
                borderStroke,
                contentAlignment,
                cellPaddingValues,
                backgroundColor,
                foregroundColor,
                content
            )
        }
    }

    @Composable
    fun TableRow(
        modifier: Modifier = Modifier,
        borderStroke: BorderStroke = this.borderStroke,
        contentAlignment: Alignment = this.contentAlignment,
        cellPaddingValues: PaddingValues = this.cellPaddingValues,
        content: @Composable TableRowScope.() -> Unit
    ) {
        columnScope.apply {
            Row(modifier.fillMaxWidth()) {
                // Create the row scope
                val tableRowScope = TableRowScope(
                    this@Row,
                    this@DataTableScope,
                ).also { tableRowScope ->
                    tableRowScope.borderStroke = borderStroke
                    tableRowScope.contentAlignment = contentAlignment
                    tableRowScope.cellPaddingValues = cellPaddingValues
                }
                // Invoke content scoped
                content.invoke(tableRowScope)
            }
        }
    }

    @Composable
    fun ColorTableRow(
        modifier: Modifier = Modifier,
        borderStroke: BorderStroke = this.borderStroke,
        contentAlignment: Alignment = this.contentAlignment,
        cellPaddingValues: PaddingValues = this.cellPaddingValues,
        backgroundColor: Color,
        foregroundColor: Color,
        content: @Composable TableRowScope.() -> Unit
    ) = TableRow(
        modifier.background(backgroundColor),
        borderStroke,
        contentAlignment,
        cellPaddingValues
    ) {
        CompositionLocalProvider(LocalContentColor provides foregroundColor) {
            // Invoke content scoped
            content.invoke(this)
        }
    }

    @Composable
    fun HeadingTableRow(
        modifier: Modifier = Modifier,
        borderStroke: BorderStroke = this.borderStroke,
        contentAlignment: Alignment = Alignment.Center,
        cellPaddingValues: PaddingValues = this.cellPaddingValues,
        backgroundColor: Color = MaterialTheme.colorScheme.primary,
        foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
        content: @Composable TableRowScope.() -> Unit
    ) = ColorTableRow(
        modifier,
        borderStroke,
        contentAlignment,
        cellPaddingValues,
        backgroundColor,
        foregroundColor,
        content
    )


    @Composable
    fun SubheadingTableRow(
        modifier: Modifier = Modifier,
        borderStroke: BorderStroke = this.borderStroke,
        contentAlignment: Alignment = Alignment.Center,
        cellPaddingValues: PaddingValues = this.cellPaddingValues,
        backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
        foregroundColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        content: @Composable TableRowScope.() -> Unit
    ) = ColorTableRow(
        modifier,
        borderStroke,
        contentAlignment,
        cellPaddingValues,
        backgroundColor,
        foregroundColor,
        content
    )
}

@Composable
fun DataTable(
    modifier: Modifier = Modifier,
    borderStroke: BorderStroke = BorderStroke(0.dp, MaterialTheme.colorScheme.onBackground),
    contentAlignment: Alignment = Alignment.Center,
    cellPaddingValues: PaddingValues = PaddingValues(all = 0.dp),
    content: @Composable DataTableScope.() -> Unit
) {
    Column(modifier) {
        // Create this table scope
        val dataTableScope = DataTableScope(
            this,
            borderStroke,
            contentAlignment,
            cellPaddingValues
        )
        // Invoke content scoped
        content.invoke(dataTableScope)
    }
}

@Composable
@Preview(showBackground = true)
fun DataTablePreview() {
    LeishmaniappTheme {
        DataTable(modifier = Modifier.padding(16.dp)) {
            HeadingTableRow {
                Cell { Text(text = "Hello World!") }
            }
            SubheadingTableRow {
                Cell { Text(text = "From") }
                Cell { Text(text = "DataTable") }
            }
            TableRow {
                Cell { Text(text = "Using") }
                SubheadingCell { Text(text = "Jetpack") }
                HeadingCell { Text(text = "Compose") }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DataTablePreview_CustomBorders() {
    LeishmaniappTheme {
        DataTable(
            modifier = Modifier.padding(16.dp),
            borderStroke = BorderStroke(0.dp, Color.Transparent)
        ) {
            HeadingTableRow {
                Cell { Text(text = "Using custom borders!") }
            }

            ColorTableRow(
                backgroundColor = MaterialTheme.colorScheme.error,
                foregroundColor = MaterialTheme.colorScheme.onError
            ) {
                borderStroke = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.errorContainer
                )

                Cell { Text(text = "Apply") }
                Cell { Text(text = "To all") }
                Cell { Text(text = "Child cells") }
            }

            HeadingTableRow(
                borderStroke = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground
                )
            ) {
                borderStroke = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.background
                )
                Cell(modifier = Modifier.padding(1.dp)) {
                    Text(text = "Combined")
                }
                Cell(modifier = Modifier.padding(1.dp)) {
                    Text(text = "Borders")
                }
            }


            TableRow {
                Cell { Text(text = "No") }
                SubheadingCell { Text(text = "Borders") }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DataTablePreview_WithAlignment() {
    LeishmaniappTheme {
        DataTable(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            HeadingTableRow {
                Cell { Text(text = "Headings have Center alignment by default") }
            }
            TableRow {
                Cell(contentAlignment = Alignment.CenterStart) { Text(text = "Left") }
                Cell(contentAlignment = Alignment.Center) { Text(text = "Center") }
                Cell(contentAlignment = Alignment.CenterEnd) { Text(text = "Right") }
            }
            SubheadingTableRow {
                Cell { Text(text = "From") }
                Cell { Text(text = "DataTable") }
            }
            TableRow {
                Cell { Text(text = "Apply") }
                SubheadingCell { Text(text = "To") }
                HeadingCell { Text(text = "Row") }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DataTablePreview_WithPadding() {
    LeishmaniappTheme {
        DataTable(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            HeadingTableRow {
                Cell { Text(text = "Cells can have Padding") }
            }
            TableRow(cellPaddingValues = PaddingValues(16.dp)) {
                Cell { Text(text = "Lorem") }
                Cell { Text(text = "Ipsum") }
                Cell { Text(text = "Dolor") }
            }
        }
    }
}