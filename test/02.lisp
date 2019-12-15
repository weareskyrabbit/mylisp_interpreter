(define leafCountCps
    (lambda (tree cont)
        (if (isPair tree)
            (leafCountCps (car tree)
                (lambda (n)
                    (leafCountCps (cdr tree)
                        (lambda (m) (cont (add n m))))))
            (cont 1))))
(leafCountCps (quote ((a . b) ((c . d) . e))) display)