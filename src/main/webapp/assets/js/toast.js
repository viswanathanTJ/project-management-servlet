"use strict";
// Below code can be easily used in vanilla js, you just need to copy below code in your js file, and remove all interfaces & types. (': void', ': string' etc... are types too)
class SwipeHandler {
    getSwipeDirection({ touchstartX, touchstartY, touchendX, touchendY }) {
        const delx = touchendX - touchstartX;
        const dely = touchendY - touchstartY;
        if (Math.abs(delx) > Math.abs(dely)) {
            return delx > 0 ? 'right' : 'left';
        }
        if (Math.abs(delx) < Math.abs(dely)) {
            return dely > 0 ? 'down' : 'up';
        }
        return 'tap';
    }
}
const svgIcons = [
    {
        name: 'check-circle',
        svg: `
      <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' viewBox='0 0 16 16'>
        <path d='M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z'/>
      </svg>
    `,
    },
    {
        name: 'info-circle',
        svg: `
      <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' viewBox='0 0 16 16'>
        <path d='M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z'/>
      </svg>
    `,
    },
    {
        name: 'exclamation-circle',
        svg: `
      <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' viewBox='0 0 16 16'>
        <path d='M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8 4a.905.905 0 0 0-.9.995l.35 3.507a.552.552 0 0 0 1.1 0l.35-3.507A.905.905 0 0 0 8 4zm.002 6a1 1 0 1 0 0 2 1 1 0 0 0 0-2z'/>
      </svg>
    `,
    },
    {
        name: 'exclamation-triangle',
        svg: `
      <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' viewBox='0 0 16 16'>
        <path d='M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z'/>
      </svg>
    `,
    },
    {
        name: 'x-lg',
        svg: `
      <svg xmlns='http://www.w3.org/2000/svg' class='t-close' width='16' height='16' fill='currentColor' viewBox='0 0 16 16'>
        <path d='M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z'/>
      </svg>
    `,
    },
];
class ToastsFactory {
    constructor(swipeHandler) {
        this.swipeHandler = swipeHandler;
        this.createToastsContainer();
        // below line enable toasts creation from buttons; can be removed if useless
        this.createToastsFromButtons();
    }
    createToastsContainer() {
        const toastsContainer = document.createElement('div');
        toastsContainer.classList.add('toasts-container');
        this.toastsContainer = toastsContainer;
        document.querySelector('body').appendChild(toastsContainer);
    }
    createToastsFromButtons() {
        document.addEventListener('click', (e) => {
            // check is the right element clicked
            if (!e.target.matches('.btn-toast'))
                return;
            const dataset = e.target.dataset;
            const config = {
                type: dataset.type,
                icon: dataset.icon,
                message: dataset.message,
                duration: dataset.duration ? parseInt(dataset.duration, 10) : undefined,
            };
            this.createToast(config);
        }, false);
    }
    createToast({ type, icon, message, duration }) {
        const toast = this.createToastContainer(type);
        this.addToastElement(toast, 't-icon', svgIcons.find((item) => item.name === icon).svg);
        this.addToastElement(toast, 't-message', message);
        this.addCloseButton(toast);
        const progressBar = this.getProgressBar(duration);
        progressBar && toast.appendChild(progressBar);
        // toasts can be closed on right swipe, comment this line if it's useless in your case
        this.observeSwipe(toast, 'right');
        this.toastsContainer.appendChild(toast);
        // if duration is 0, toast message will not be closed automatically
        if (!progressBar)
            return;
        progressBar.onanimationend = () => this.removeToast(toast);
    }
    createToastContainer(type) {
        const toast = document.createElement('div');
        toast.classList.add('toast', type, 'active');
        return toast;
    }
    addToastElement(toast, className, content) {
        const element = document.createElement('div');
        element.classList.add(className);
        element.innerHTML = content;
        toast.appendChild(element);
        return element;
    }
    addCloseButton(toast) {
        const closeButton = this.addToastElement(toast, 't-close', svgIcons.find((icon) => icon.name === 'x-lg').svg);
        closeButton.onclick = () => this.removeToast(toast);
    }
    getProgressBar(duration) {
        // duration is infinite => no progress bar
        if (duration === 0)
            return;
        const progressBar = document.createElement('div');
        progressBar.classList.add('t-progress-bar');
        // custom duration need's to be added in scss file (line 146)
        duration && progressBar.classList.add(`t-${duration}`);
        return progressBar;
    }
    removeToast(toast) {
        toast.classList.remove('active');
        // we wait for the end of the animation to remove the element from the DOM
        toast.onanimationend = (evt) => {
            // needed to filter progressBar animation
            evt.target === toast && toast.remove();
        };
    }
    observeSwipe(toast, direction) {
        let touchstartX = 0, touchstartY = 0, touchendX = 0, touchendY = 0;
        toast.addEventListener('touchstart', (event) => {
            // needed to avoid weird behavior on swipe
            window.document.body.style.overflow = 'hidden';
            touchstartX = event.changedTouches[0].screenX;
            touchstartY = event.changedTouches[0].screenY;
        }, { passive: true });
        toast.addEventListener('touchend', (event) => {
            window.document.body.style.overflow = 'unset';
            touchendX = event.changedTouches[0].screenX;
            touchendY = event.changedTouches[0].screenY;
            const swipeConfig = {
                touchstartX,
                touchstartY,
                touchendX,
                touchendY,
            };
            this.swipeHandler.getSwipeDirection(swipeConfig) === direction && this.removeToast(toast);
        }, { passive: true });
    }
}
