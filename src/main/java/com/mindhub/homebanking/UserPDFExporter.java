package com.mindhub.homebanking;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPDFExporter {
    private List<Transaction> transactionList;

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Transaction ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            String amount;
            if(transaction.getType().equals(TransactionType.Debit)) {
                amount = "-" + String.valueOf(transaction.getAmount());
            } else {
                amount = "+" + String.valueOf(transaction.getAmount());
            }
            table.addCell(String.valueOf(transaction.getId()));
            table.addCell(transaction.getType().toString());
            table.addCell(amount);
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getDate()));
        }
    }

}


