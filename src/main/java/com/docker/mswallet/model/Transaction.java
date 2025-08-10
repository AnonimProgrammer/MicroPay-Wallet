package com.docker.mswallet.model;

import java.util.UUID;

public class Transaction {

    private UUID id;
    private Long senderId;
    private Long receiverId;
    private Double amount;
    private Status status;

    public Transaction() {}
    public Transaction(Long senderId, Long receiverId, Double amount, Status status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    // Getters
    public UUID getId() {
        return id;
    }
    public Long getSenderId() {
        return senderId;
    }
    public Long getReceiverId() {
        return receiverId;
    }
    public Double getAmount() {
        return amount;
    }
    public Status getStatus() {
        return status;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
