package com.example.shoppingweb;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class Main extends HttpServlet {
    private List<Product> products;

    @Override
    public void init() throws ServletException {
        // 初始化商品数据 - 现在使用6个参数
        products = Arrays.asList(
                new Product(1, "iPhone 14 Pro Max", "A16芯片，4800万像素主摄", 8999.00, "https://picsum.photos/300/200?random=1", 50),
                new Product(2, "MacBook Pro 16寸", "12核CPU，19核GPU，32GB内存", 18999.00, "https://picsum.photos/300/200?random=2", 30),
                new Product(3, "AirPods Pro 2", "主动降噪，空间音频", 1899.00, "https://picsum.photos/300/200?random=3", 100)
        );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();

        if ("/products".equals(path)) {
            request.setAttribute("products", products);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/product.html");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
            dispatcher.forward(request, response);
        }
    }
}