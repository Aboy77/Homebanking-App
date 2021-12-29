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
            data: [],
            cards: [],
            count: 0,
            cardList: [],
            credit: null,
            debit: null,
            type: "",
            color: "",
            fullCards: false,
            dateNow: {
                currentYear: new Date().getFullYear(),
                currentDay: new Date().getDate()
            },
            accounts: [],
            account: ""
        }
    },
    created() {
        this.loadClients();
    },
    methods: {
        loadClients() {
            let currYearS = this.dateNow.currentYear.toString();
            let currDay = this.dateNow.currentDay;

            let year = currYearS.substring(2, 3) + currYearS.substring(3, 4)
            let currYear = Number(year);

            axios.get("/api/clients/current")
                .then(res => {
                    this.data = res.data;
                    this.accounts = res.data.accounts;
                    console.log(this.accounts)
                    this.cards = res.data.cards;
                    this.cards.forEach(card => {
                        //simplificar el año de vencimiento para la vista en interfaz de la tarjeta
                        let from = card.thruDate;
                        let n = from.split("");
                        card.thruDate = n[8] + n[9] + "/" + n[2] + n[3];

                        //vericar si una tarjeta esta vencida o no
                        //obtener el año y el dia
                        let year = n[2] + n[3];
                        let day = n[8] + n[9]

                        //convertirlo a tipo Number
                        let yearNumber = Number(year);
                        let dayNumber = Number(day);


                        //realizar la condicion para detectar una fecha vencida
                        //si la condicion del año y dia de vencimiento se cumple se agregara un campo de tipo booleano a las tarjetas
                        if (yearNumber <= currYear && dayNumber <= currDay) {
                            card.thru = true;
                        } else {
                            card.thru = false;
                        }

                    })

                    this.cards.sort((a, b) => {
                        if (a > b) {
                            return -1
                        }
                        if (a < b) {
                            return 1
                        }
                        return 0
                    })

                    if (this.cards.length >= 2) {
                        this.credit = true;
                        this.debit = false;
                    }

                    if (this.cards.length === 6) {
                        this.fullCards = true
                    }


                })
        },
        numbers(n) {
            let c1 = n.toString()
            return c1.substring(0, 4) + " " + c1.substring(4, 8) + " " + c1.substring(8, 12) + " " + c1.substring(13, 17);
        },
        cardEvents(e) {
            if (this.credit) {
                this.cards = this.cards.filter(card => card.type !== "debit");
            }

            let div = e.path.filter(c => c.id)

            const cardFront = div[2].querySelector("#card-front");
            const cardBack = div[2].querySelector("#card-back");

            if (this.count <= 1) {
                this.count++;
            } else {
                this.count = 1
            }

            if (this.count === 1) {
                cardBack.style.transform = "rotateY(0deg)"
                cardFront.style.transform = "rotateY(180deg)"
                cardFront.style.opacity = "0"
                cardBack.style.opacity = "1"
            }

            if (this.count === 2) {
                cardFront.style.transform = "rotateY(0deg)"
                cardBack.style.transform = "rotateY(180deg)"
                cardFront.style.opacity = "1"
                cardBack.style.opacity = "0"
            }
        },
        credits() {
            this.credit = false;
        },
        debits() {
            this.debit = true;
        },
        showCards(e) {
            console.log(e.target)
        },
        createCard() {
            window.location.href = "/web/create-card.html"
        },
        orderCard(e) {
            e.target.disabled = true;
            axios.post("/api/clients/current/cards", `type=${this.type}&color=${this.color}&numberAcc=${this.account}`).then(res => {
                swal({
                    title: "Card ordered!",
                    text: res.data + "!",
                    icon: "success",
                    button: "OK!",
                }).then(res => {
                    e.target.disabled = false;
                    window.location.href = "/web/cards.html";
                });
            }).catch(err => {
                console.log(err.response.status)
                swal({
                    title: "Warning!",
                    text: err.response.data + "!",
                    icon: "warning",
                    button: "Check!",
                }).then(res => {
                    e.target.disabled = false;
                })

                if (err.response.status == 400) {
                    swal({
                        title: "Error!",
                        text: "Empty fields!",
                        icon: "error",
                        button: "Check!",
                    }).then(res => {
                        e.target.disabled = false;
                    })
                }
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


app.mount("#app")