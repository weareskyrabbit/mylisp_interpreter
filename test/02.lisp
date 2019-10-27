(define leaf-count/cps
    (lambda (tree cont)
        (if (pair? tree)
            (leaf-count/cps (car tree)
                (lambda (n)
                    (leaf-count/cps (cdr tree)
                        (lambda (m) (cont (+ n m))))))
            (cont 1))))
(leaf-count/cps '((a . b) (c . d) . e) display)