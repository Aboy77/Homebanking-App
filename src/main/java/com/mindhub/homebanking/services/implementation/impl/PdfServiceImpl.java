package com.mindhub.homebanking.services.implementation.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.implementation.PdfService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Set;


@Service
public class PdfServiceImpl implements PdfService {

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
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

    private void writeTableData(PdfPTable table, Set<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            table.addCell(String.valueOf(transaction.getId()));
            table.addCell(transaction.getType().toString());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getDate()));
        }
    }

    public void exports(HttpServletResponse response, Set<Transaction> transactions, Client client) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.MAGENTA);

        Paragraph p = new Paragraph("List of Transactions", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 2.8f, 2.0f, 3.0f, 2.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, transactions);

        document.add(table);

        document.close();
    }
}
