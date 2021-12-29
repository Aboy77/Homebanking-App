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
            axios.get("/api/logout").then(res => console.log(res))
            window.location.href = "/"
            break;
    }
})

const app = Vue.createApp({
    data() {
        return {
            reason: "",
            body: ""
        }
    },
    created() {

    },
    methods: {
        sendForm() {
            axios.post(`/form/send?subject=${this.reason}&body=${this.body}`).then(res => {
                console.log(res)
                swal({
                    title: "Success!",
                    text: res.data + "!",
                    icon: "success",
                    button: "OK!",
                }).then(res => {
                    window.location.href = "/web/accounts.html";
                });
            }).catch(err => {
                console.log(err.response)
            })
        }
    }
})

app.mount("#app")