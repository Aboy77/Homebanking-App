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

const app = Vue.createApp({
    data() {
        return {
            test: "hello world",
            data: [],
            loans: [],
            payments: [],
            accounts: [],
            loanData: {
                name: "",
                id: "",
                amount: "",
                payments: "",
                number: "",
                description: ""

            },
            total: 0
        }
    },
    created() {
        axios.get("/api/clients/current").then(res => {
            this.data = res.data;
            this.accounts = res.data.accounts;
        }).catch(err => console.log(err))
        this.loadLoans();
    },
    methods: {
        loadLoans() {
            axios.get("/api/loans").then(res => {
                this.loans = res.data;
            }).catch(err => console.log(err))
        },
        loadInfo(e) {
            switch (e.target.type) {
                case "radio":
                    this.loanData.name = e.target.value;
                    this.loanData.id = e.target.id;
                    let filter = this.loans.filter(pay => pay.id == this.loanData.id)
                    this.payments = filter[0].payments;
                    break;
                case "text":
                    this.loanData.description = e.target.value;
                    break;
                case "number":
                    this.loanData.amount = e.target.value;
                    let number = Number(e.target.value)

                    //realizar el calculo segun el tipo de prestamo seleccionado
                    //filtrar por el nombre del prestamo seleccionado
                    let getLoan = this.loans.filter(l => l.name == this.loanData.name);

                    //realizar el calculo con el prestamo seleccionado
                    this.total = ((number * getLoan[0].percent) / 100) + number;
                    break;
            }
            switch (e.target.name) {
                case "name":
                    this.loanData.payments = e.target.value;
                    break;
                case "account":
                    this.loanData.number = e.target.value;
                    break;
            }
        },
        sendApplication(e) {
            axios.post("/api/loans", { id: this.loanData.id, name: this.loanData.name, amount: this.loanData.amount, payments: this.loanData.payments, number: this.loanData.number, description: this.loanData.description }).then(res => {
                window.location.href = "/web/accounts.html"
            }).catch(err => {
                console.log(err.response.data)
                const div = document.getElementById("err");
                div.innerText = err.response.data.error;
                div.style.display = "block";

                setTimeout(() => {
                    div.style.display = "none";
                }, 3000)
            })
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
        }
    }
})

app.mount("#app");