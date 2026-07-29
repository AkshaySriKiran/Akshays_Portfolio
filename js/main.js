(() => {
  const year = document.getElementById("year");
  if (year) year.textContent = String(new Date().getFullYear());

  const header = document.querySelector(".site-header");
  const onScroll = () => {
    if (!header) return;
    header.classList.toggle("is-scrolled", window.scrollY > 12);
  };
  onScroll();
  window.addEventListener("scroll", onScroll, { passive: true });

  const reveals = Array.from(document.querySelectorAll("[data-reveal]"));
  const show = (el) => el.classList.add("is-visible");

  const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  if (reduceMotion || !("IntersectionObserver" in window)) {
    reveals.forEach(show);
    return;
  }

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;
        show(entry.target);
        observer.unobserve(entry.target);
      });
    },
    { threshold: 0.01, rootMargin: "0px 0px 40px 0px" }
  );

  reveals.forEach((el, index) => {
    el.style.transitionDelay = `${Math.min(index % 5, 4) * 50}ms`;
    observer.observe(el);
  });

  // Mobile Safari / delayed layout fallback — never leave content invisible
  window.setTimeout(() => {
    reveals.forEach((el) => {
      if (!el.classList.contains("is-visible")) show(el);
    });
  }, 600);
})();
