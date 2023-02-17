package com.example.zavrsnirad

import androidx.compose.ui.graphics.Color

data class TransactionType(
    val name: String,
    val categoryIconString: String,
    val backgroundColor: Color,
    val textBackgroundColor: Color
)

val transactionCategories = listOf(
    TransactionType("FOOD", "\uD83E\uDD59", Color(0xFFFF5D5D), Color(0xFFFF9C9C)),
    TransactionType("BILLS", "\uD83D\uDCB6", Color(0xFF4B93FF), Color(0xFF91E0FF)),
    TransactionType("INCOME", "\uD83E\uDE99", Color(0xFFFFA84B), Color(0xFFFFCA91)),
    TransactionType("CLOTHES", "\uD83D\uDC55", Color(0xFF5DFF4B), Color(0xFF91FF96)),
    TransactionType("TRANSPORT", "\uD83D\uDE8C", Color(0xFFFFE74B), Color(0xFFFFED91)),
    TransactionType("HOUSING", "\uD83C\uDFE0", Color(0xFF9F4BFF), Color(0xFFD591FF)),
    TransactionType("VEHICLE", "\uD83D\uDE97", Color(0xFFFF6F4B), Color(0xFFFF9191)),
    TransactionType("INVESTMENTS", "\uD83C\uDFE6", Color(0xFFFF4B8A), Color(0xFFFF91DA)),
    TransactionType("OTHER", "\uD83D\uDCCB", Color(0xFF797979), Color(0xFFB6B6B6)),

)

/*
public class FoodType{
    val name: String = "FOOD";
    val categoryIconString = "\uD83E\uDD59";
    val backgroundColor: Color = Color(0xFFFF5D5D);
    val textBackgroundColor: Color = Color(0xFFFFADAD);
}

public class TransportType{
    val name: String = "TRANSPORT";
    val categoryIconString = "\uD83D\uDE8C";
    val backgroundColor: Color = Color(0xFFFFEF5D);
    val textBackgroundColor: Color = Color(0xFFFFEBAD);
}*/