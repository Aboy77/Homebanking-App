const info = document.querySelector(".info");

document.addEventListener("mousemove", e => {
        if (e.pageX < 90) {
                info.style.opacity = "1"
                info.style.width = "90px";
        } else {
                info.style.opacity = "0"
                info.style.width = "0px";
        }
})
const newPage = document.getElementById("newPage");

newPage.addEventListener("click", e => {
        switch (e.target.id) {
                case "home":
                        window.location.href = "/web/home.html"
                        break;
                case "accounts":
                        window.location.href = "/web/accounts.html"
                        break;
                case "cards":
                        window.location.href = "/web/cards.html"
                        break;
                case "transfer":
                        window.location.href = "/web/loan-application.html"
                        break;
                case "create-c":
                        window.location.href = "/web/create-card.html"
                        break;
                case "support":
                        window.location.href = "/web/support.html"
                        break;
        }
})

// log out
const div = document.getElementById("log-out");

div.addEventListener("click", (e) => {
        e.preventDefault();
        axios.get("/api/logout").then(res => window.location.href = "/").catch(err => console.log(err))
})

const app = Vue.createApp({
        data() {
                return {
                        data: [],
                        accounts: [],
                        accountsCreate: true,
                        loans: false,
                        loan: [],
                        deleteAcc: false,
                        create: false,
                        type: ""
                }
        },
        created() {
                this.loadData();
        },
        computed: {

        },
        methods: {
                loadData() {
                        axios.get("/api/clients/current")
                                .then(data => {
                                        this.data = data.data;
                                        this.accounts = data.data.accounts;
                                        console.log(this.accounts)
                                        this.accounts.sort((a, b) => {
                                                if (a["id"] < b["id"]) {
                                                        return -1;
                                                } else if (a["id"] > b["id"]) {
                                                        return 1
                                                } else {
                                                        return 0
                                                }
                                        })
                                        if (data.data.loans.length > 0) {
                                                this.loans = true;
                                                this.loan = data.data.loans;
                                                console.log(this.loan)
                                        }
                                });
                },
                showAccounts(i) {
                        let id = i;
                        window.location.href = `./account.html?id=${id}`
                },
                showCards(e) {
                        this.loans = false;
                        this.accountsCreate = false;
                },
                logout(e) {
                        axios.post("/api/logout")
                                .then(res => {
                                        window.location.href = "/index.html"
                                })
                                .catch(err => console.log(err))
                },
                createAccount() {
                        axios.post("/api/clients/current/account").then(res => console.log(res)).catch(err => console.log(err))
                },
                loanApplication(e) {
                        window.location.href = "/web/loan-application.html"
                },
                newPage(e) {
                        switch (e.target.id) {
                                case "home":
                                        window.location.href = "/web/home.html"
                                        break;
                                case "accounts":
                                        window.location.href = "/web/accounts.html"
                                        break;
                                case "cards":
                                        window.location.href = "/web/cards.html"
                                        break;
                                case "transfer":
                                        window.location.href = "/web/transaction.html"
                                        break;
                                case "create-c":
                                        window.location.href = "/web/create-card.html"
                                        break;
                                case "support":
                                        window.location.href = "/web/support.html"
                                        break;
                        }
                },
                deleteAccount(e) {
                        console.log(e.target.id)
                        axios.delete(`/api/clients/current/account/delete?number=${e.target.id}`).then(res => {
                                swal({
                                        title: "Deleted!",
                                        text: res.data + "!",
                                        icon: "success",
                                        button: "OK!",
                                }).then(res => {
                                        window.location.reload();
                                })
                        }).catch(err => {
                                swal({
                                        title: "Error!",
                                        text: err.response.data + "!",
                                        icon: "error",
                                        button: "OK!",
                                }).then(res => {
                                        window.location.reload();
                                })
                        })
                },
                createAccount(e) {
                        this.create = true;
                        document.querySelector("body").style.overflow = "hidden";
                },
                createBack() {
                        this.create = false;
                },
                createAcc(e) {
                        this.create = false;
                        axios.post(`/api/clients/current/account?accountType=${this.type}`).then(res => {
                                swal({
                                        title: "Created!",
                                        text: res.data + "!",
                                        icon: "success",
                                        button: "OK!",
                                }).then(res => {
                                        window.location.reload();
                                })
                        }).catch(err => {
                                swal({
                                        title: "Error!",
                                        text: err.response.data + "!",
                                        icon: "error",
                                        button: "OK!",
                                }).then(res => {
                                        window.location.reload();
                                })
                        })
                },
                donwloadTransactions(e) {
                        axios.get("/pdf/transactions/download?number=1").then(res => {
                                console.log(res)
                        }).catch(err => {

                        })
                }
        }
})

app.mount("#app");