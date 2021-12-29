const info = document.querySelector(".info");
console.log(info)

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

let balance;

const app = Vue.createApp({
        data() {
                return {
                        clients: [],
                        accounts: [],
                        transactions: [],
                        investAnnual: 0,
                        invest: false,
                        account: true,
                        transfer: false,
                        exchange: [],
                        amount: 0,
                        transferInfo: {
                                send: 0,
                                accounts: "",
                                selected: "",
                                description: "",
                                cbu: ""
                        },
                        current: ""
                }
        },
        created() {
                this.loadAccount();
                this.loadClient();

                axios.get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
                        .then(res => {
                                this.exchange = [res.data[0], res.data[1], res.data[5]]
                        })
        },
        methods: {
                loadAccount() {
                        const url = window.location.search;
                        const param = new URLSearchParams(url);
                        const id = param.get("id");

                        axios.get(`/api/accounts/${id}`)
                                .then(res => {
                                        console.log(res)
                                        this.accounts = res.data;
                                        this.transactions = res.data.transactions;
                                        this.current = res.data.number;
                                })
                },
                loadClient() {
                        axios.get("/api/clients/current", { headrs: { 'accept': 'application/xml' } })
                                .then(res => {
                                        console.log(res.data)
                                        this.clients = res.data;
                                        this.transferInfo.accounts = res.data.accounts.filter(acc => acc.number != this.current);
                                })
                },
                showCards(e) {
                        console.log(e.target)
                },
                investment(e) {
                        this.account = false;
                        this.invest = true;
                },
                downloadResume(e) {
                        let url = window.location.search;
                        let path = new URLSearchParams(url);
                        let id = path.get("id");

                        window.location.href = `/pdf/transactions/download?number=${id}`
                },
                amountInvest(e) {
                        this.amount = parseInt(e.target.value)
                },
                sendInvest() {
                        const url = window.location.search
                        const param = new URLSearchParams(url)
                        const id = param.get("id");
                        const idNumber = Number(id);

                        axios.post(`/api/transfer?id=${idNumber}&amount=${this.amount}&cbu=${this.transferInfo.cbu}`).then(res => {
                                swal({
                                        title: "Sended!",
                                        text: res.data + "!",
                                        icon: "success",
                                        button: "OK!",
                                }).then(res => {
                                        window.location.href = `/web/account.html?id=${id}`
                                })
                        }).catch(err => {
                                swal({
                                        title: "Error!",
                                        text: err.response.data,
                                        icon: "error",
                                        button: "OK!",
                                })
                                this.transferInfo.cbu = "";
                        })

                },
                backAccount() {
                        this.account = true;
                        this.invest = false;
                },
                createBack() {
                        this.transfer = false;
                        this.account = true;
                },
                transferEvent(e) {

                        this.amount = parseInt(e.target.value);
                        if (!isNaN(this.amount)) {
                                this.transferInfo.send = this.amount.toFixed(2)
                        }
                },
                sendTransfer(e) {
                        console.log(this.transferInfo.send)
                        axios.post("/api/transactions", `number=${this.transferInfo.selected}&amount=${this.transferInfo.send}&current=${this.current}&description=${this.transferInfo.description}`).then(res => {
                                swal({
                                        title: "Sended!",
                                        text: res.data + "!",
                                        icon: "success",
                                        button: "OK!",
                                }).then(res => {
                                        this.transfer = false;
                                        this.account = true;
                                        this.amount = 0;
                                        this.transferInfo.send = 0;
                                        this.loadAccount();
                                })

                        }).catch(err => {
                                const div = document.getElementById("err");
                                div.innerText = err.response.data;
                                div.style.display = "block";

                                setTimeout(() => {
                                        div.style.display = "none";
                                }, 3000)
                        })
                },
                transferE(e) {
                        this.transfer = true;
                        this.account = false;
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
                downloadTransaction(e) {
                        let id = e.target.id;
                        window.location.href = `/pdf/transactions/download?number=${id}`
                }
        }
})

app.mount("#app");