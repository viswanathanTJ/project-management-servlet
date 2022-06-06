// very simple cookie manager class
class CookieManager {
  static setCookie({ name = "", days = 0, value = "", path = "/" }) {
    let expire = "";
    if (days) {
      const date = new Date();
      date.setTime(date.getTime() + days * 1000 * 60 * 60 * 24);
      expire = date.toUTCString();
    }
    document.cookie = `${name}=${value}; expires=${expire}; path=${path}`;
  }

  static getCookie({ name }) {
    name = name.trim();
    const allCookie = document.cookie;
    const cookieArr = [...allCookie.split(";")];
    for (let i = 0; i < cookieArr.length; i++) {
      let c = cookieArr[i].trim();
      if (c.startsWith(`${name}=`)) return c.split("=")[1];
    }
    return null;
  }

  static removeCookie({ name = "" }) {
    CookieManager.setCookie({ name, days: -1 });
  }

  static checkCookie({ name = "" }) {
    const cookie = CookieManager.getCookie({ name: name });
    if (cookie !== undefined && cookie !== "" && cookie !== null) {
      return true;
    } else {
      return false;
    }
  }
}
//add cokie
// CookieManager.setCookie({
//   name: "hi.cookie",
//   value: "m9yhRuPk7xlCpkEGk8qdx",
//   days: 10,
// });

//get Cookie value
// CookieManager.getCookie({ name: "hi.cookie" }); // return = m9yhRuPk7xlCpkEGk8qdx
//remove cookie
// CookieManager.removeCookie({ name: "hi.cookie" }); // noting return
// CookieManager.checkCookie({ name: "hi.cookie" });
