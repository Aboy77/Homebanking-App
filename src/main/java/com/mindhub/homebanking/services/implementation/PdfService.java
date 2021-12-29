package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.models.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public interface PdfService {
    public void exports(HttpServletResponse response, Set<Transaction> transactions, Client client) throws IOException;
}
