(define factorial
    (lambda (n)
        (if (equal n 0)
            1
            (mul n
                (factorial (sub n 1))))))
(factorial 5)
