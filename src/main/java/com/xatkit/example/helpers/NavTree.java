package com.xatkit.example.helpers;

import java.util.*;
import com.xatkit.example.helpers.CustomSb;
import static com.xatkit.example.helpers.Utils.*;


// if not specified, la source vient de wikipedia
// rendering latex in markdown file
// https://gist.github.com/jesshart/8dd0fd56feb6afda264a0f7c3683abbf

public class NavTree
{
    public Node[] nodes;

    // for testing purposes
    public static void main( String[] args )
    {
        NavTree t = init_nav_3();
        System.out.println( 
            t.navigate( MATH ).navigate( MATH_CALC_I ).navigate ( "C'est quoi la dérivation ?" ).nodes[0].header );
    }

    // inits the default nav3 
    public static NavTree init_nav_3 ( )
    {
        final int size = 3;

        Node[] nodes_ = new Node[size];

        // NOTE : toutes vos questions doivent finir par un espace et une marque d'interrogation : <<" ?">>
        nodes_[0] = new Node (MATH);
        nodes_[0].fillNodes( new Node[] {
            new Node(MATH_CALC_I).fillNodes (
                // inserer les questions ici de CALCULUS I
                new Node[] {
                    new Node ( "C'est quoi la dérivation ?" ).fillNodes (
                        new String[] {
                            new CustomSb(
                                "La dérivation est un moyen de connaître la pente "
                            ).append(
                                "``( le taux de variation )`` d'une fonction en un point de cette dernière."
                            )
                            .appendN ( 
                                "" 
                            ).out(),
                        }
                    ),
                    new Node ( "Comment calculer une dérivée ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Le calcul d'une dérivé repose fondamentalement sur la technique suivante"
                            ).appendN(
                                "$$f^{/prime} = \\lim_{h \\to 0} \\frac{f(x+h) - f(x)}{h}$$"
                            ).appendN(
                                "Tel que vu au secondaire, le calcul d'une pente quelconque se fait à "
                            ).append(
                                "l'aide de deux pentes. Dans la formule ci-dessus, il est question de prendre "
                            ).append(
                                "la différence entre deux points qui se rapprochent infiniment ( d'où le **h** qui "
                            ).appendN(
                                "tend vers des valeurs de plus en plus petites."
                            ).append(
                                "Cependant, avec des fonctions plus complexes, le calcul limite "
                            ).append(
                                "devient de plus en plus fastidieux. Il te faudra alors de mémoriser "
                            ).append(
                                "les formules fournies par ton prof."
                            ).out(),
                        }
                    ),
                    new Node ( "D'où vient le symbole de la dérivation ?").fillNodes (
                        new String[] {
                            "Il existe plusieurs notations de la dérivation. Les voici",
                            new CustomSb("").append(
                                "" 
                            ).append(
                                "- La notation de **Lagrange** "
                            ).appendN(
                                "```math"
                            ).appendN(
                                "f{\\prime} ( x )```"
                            ).appendN(
                                "- La notation de **Leibniz**  : $\\frac{df}{dx}$"
                            ).appendN(
                                "- La notation d'**Euler**     : $\\D_x f ( a )"
                            ).appendN(
                                "- La notation de **Newton**   : $\\dot{f} ( x )"
                            ).out(),
                            "Psst, je te conseillerai d'aller avec la notation que ton prof. utilise 😉"
                        }
                    ),
                    new Node ( "Qui est l'inventeur du calcul I ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Entre **Isaac Newton** et **Gottfried Leibniz**, cette question "
                            ).append(
                                "fait preuve de plusieurs débats et ce depuis le 18 ième siècle"
                            ).out(),
                        }
                    ),
                    new Node ( "Quelle est l'application de la dérivée ?").fillNodes (
                        new String[] {
                            "Voici quelques unes des applications de la dérivée",
                            new CustomSb("").appendN(
                                "- Dans la mécanique 👨‍🔧"
                            ).appendN(
                                "- Dans l'intelligence artificielle 🤖"
                            ).append(
                                "- Dans l'économie 💱"
                            ).out(),
                        }
                    ),
                }
            ),
            new Node( MATH_CALC_II ).fillNodes(
                // inserer les questions ici de CALCULUS II
                new Node[] {
                    new Node ( "C'est quoi l'intégrale ?" ).fillNodes (
                        new String[] {
                            // new CustomSb(
                            //     "Une intégrale, de façon plus simple, permet de trouver l'aire au dessous "
                            // ).append(
                            //     "d'une courbe souvent décrite à l'aide d'une fonction continue."
                            // ).out(),
                            "![something](./test.jpg)"
                        }
                    ),    
                    new Node ( "Comment calculer une intégrale ?" ).fillNodes (
                        new String[] {
                            new CustomSb(
                                "Une intégrale d'une fonction ``f`` qu'on note de la façon suivante\n"
                            ).appendN(
                                "$\\int f(x) dx = F( x ) + C$"                                
                            ).appendN(
                                "``F( x )`` est appelée la primitive de ``f`` ou bien l'anti-derivative de ``f`` d'où la notation"
                            ).appendN(
                                "$$F^{\\prime}( x ) = f( x )$$"
                            ).appendN(
                                "Il suffit donc de connaître la dérivée dans le but d'intégrer."
                            ).append(
                                "Finalement, pour résoudre des intégrales plus difficiles, il suffira de mémoriser "
                            ).append(
                                "les formules d'intégrale fournies par ton prof."
                            ).out(),
                        }
                    ),
                    new Node ( "Qui est l'inventeur de l'intégrale ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Entre **Isaac Newton** et **Gottfried Leibniz**, cette question "
                            ).append(
                                "fait preuve de plusieurs débats et ce depuis le 18 ième siècle"
                            ).out(), 
                        }
                    ),
                    new Node ( "C'est quoi l'application du calcule intégrale ?").fillNodes (
                        new String[] {
                            "Voici quelques unes des applications de la dérivée",
                            new CustomSb("").appendN(
                                "- Dans la mécanique 👨🏻‍🔧"
                            ).appendN(
                                "- Dans l'intelligence artificielle 🤖"
                            ).append(
                                "- Dans l'économie 💱"
                            ).out(),
                        }
                    ),
                }
            )
            ,
            new Node (MATH_ALGEBRE_LIN).fillNodes(
                // inserer les questions ici de ALGEBRE LINEAIRE
                new Node[] {
                    new Node ("C'est quoi une matrice ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Simplement, une matrice est une liste d'élément ( nombres, chiffres, lettres, symboles, <...> )."
                            ).appendN(
                                ""
                            ).appendN(
                                "Par exemple, ``['chient', 'chat', 'baleine', 'tigre', 'lion']`` est une matrice de ``1`` par ``5``."
                            ).append(
                                "Dans le cours Algèbre linéaire 🕹️, tu seras amené à effectuer des opérations ( avec des nombres "
                            ).append(
                                "cette fois) sur celles-ci."
                            ).out(),
                        }
                    ),
                    new Node ("Comment multiplier des matrices ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "La multiplication matricielle est définit comme suit"
                            ).appendN(
                                "$$A_{m \\times n} \\cdot B_{n \\times j} = \\sum_{k=1}^{m} a_{nk}b_{kj}$$"
                            ).append(
                                "Règle primordial : il faut que la deuxième matrice aie aie le même nombre de lignes "
                            ).append(
                                "( horizontale ) que de colonne ( verticale ) de la première matrice."
                            ).out(),
                        }
                    ),
                    new Node ("D'où vient le nom de matrice ?").fillNodes ( 
                        // https://www.britannica.com/science/matrix-mathematics
                        new String[] {
                            new CustomSb(
                                "Le nom de matrice a été introduit une première fois en mathématique par "
                            ).append(
                                "le mathématicien James Sylvester."
                            ).out(),
                        }
                    ),
                    new Node ("Quelle est l'application de l'algèbre linéaire ?").fillNodes (
                        new String[] {
                            "En voici quelques unes",
                            new CustomSb("").appendN(
                                "- Production d'engin de jeux vidéo 💻"
                            ).appendN(
                                "- Science de données 📊"
                            )
                            .appendN(
                                "- En économie 💲"
                            ).append(
                                "- En biologie 🔬"
                            ).out(),
                        }
                    ),
                    new Node ("C'est quoi une 'transposée' ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "La ``transposition`` est une opération qui consiste "
                            ).appendN(
                                "*retourner* par apport à sa diagonale."
                            ).appendN(
                                "Mathématiquement"
                            ).appendN(
                                "$$A_{m \\times n}^{T} = A_{n \\times m} \\iff \\forall x_{ij} \\in "
                            ).append(
                                "Tu peux en savoir davantage dans ton manuel respectif de ton cours."
                            ).out(),
                        }
                    ),
                    new Node ("C'est quoi un vecteur ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Une vecteur est aussi une matrice, mais elle n'a qu'une ligne ( horizontale )."
                            ).appendN(
                                ""
                            ).append(
                                "Par exemple, voiçi un vecteur de mes fruits ( fruits ? ) préférés "
                            ).append(
                                "['banane', 'pomme', 'avocat', 'fraise', 'citron']"
                            ).out(),
                        }
                    ),
                }
            ),
            new Node (MATH_DISCRETE).fillNodes(
                // inserer les questions ici des mathematiques discrete
                new Node[] {
                    new Node("Pourquoi le mot 'discrètes' ?").fillNodes (
                        // incrire les reponses ici 
                        // https://www.cs.odu.edu/~toida/nerzic/content/intro2discrete/intro2discrete.html
                        new String[] {
                            new CustomSb(
                                "Les mathématiques discrètes est une branche qui "
                            ).appendN(
                                "repose sur les **objets discrets**. "
                            ).append(
                                "Les objets discrets qu'ont peut distinguer entre eux. "
                            ).append(
                                "Par exemple, les nombres rationnels, les voitures, les maisons, les humains. "
                            ).append(
                                "Dans ce cours (⚠️SPOILER ALERT⚠️), il sera plus question de nombre entier, propositions, ensembles, <...>"
                            ).out(),
                        }
                    ),
                    new Node("C'est quoi une porte logique ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "Une porte logique est un circuit électronique réalisant des opérations logiques "
                            ).append(
                                "sur une séquence de bits ( chiffrement binaire )."
                            ).out(),
                        }
                    ),
                    new Node("C'est quoi un opérateur logique ?").fillNodes ( 
                        new String[] {
                            new CustomSb(
                                "Imagine une opération qui prend des données en entrée ( **input**, 1/unaire | 2/binaire ) "
                            ).append(
                                "et qui donnent des sorties ( **output** ). Le mot *logique* "
                            ).appendN(
                                "signifie que ces entrées subissent des conditions. Voiçi une liste d'opérateur logique"
                            ).out(),
                            new CustomSb("").appendN(
                                "- AND ($\\cdot$) -> vrai ( 1 ) lorsque les deux valeurs sont vraies."
                            ).appendN(
                                // can't seem to find the OR symbol with latex
                                "- OR ($+$) -> vrai lorsque au moins une des entrées est vraie." 
                            ).append(
                                "- XOR ($\\oplus$) -> vrai lorsque au plus une des entrées est vraie."
                            ).out(),
                        }
                    ),
                    new Node("Qu'est-ce que la cryptographie ?").fillNodes (
                        new String[] {
                            new CustomSb(
                                "La cryptographie consiste à protéger un secret, souvent un message."
                            ).out(),
                        }
                    ),
                }
            ),
        });

        nodes_[1] = new Node ( CHIMIE );
        nodes_[1].fillNodes (
          new Node[] {
            new Node(STOCH).fillNodes (
                new Node[] {
                  new Node("Qu'est-ce que la stochiometrie ?").fillNodes (
                    new String[] {
                      new CustomSb ( "la stochiometrie est une analyse des quantites des particules ")
                        .append("dans une reaction chimique.").out(),
                    }
                  ),
                  new Node("Qui est l'inventeur de la stochiometrie ?").fillNodes (
                    new String[] {
                      new CustomSb ( "Jeremias Benjamin Richter est le premier homme a enoncer ")
                      .append ( "les principes de la stochiometrie.").out(),
                    }
                  ),
                  new Node ("Quelles sont les applications de la stochiometrie ?").fillNodes (
                    new String[] {
                      new CustomSb( "Voici quelques unes des applications de la **stochiometrie**").appendN("").out(),
                      new CustomSb( "- La pharmaceutique⚕" )
                        .appendN(
                          "- L'ingenieurie chimique👩‍🔬"
                        ).appendN(
                          "- Les sciences de l'environnement🌳"
                        ).out(),
                    }
                  ),
                }
            ),
            new Node(COMP_CHIMIQUE).fillNodes (
              new Node[] {
                new Node ( "Inserer la premiere question pour " + COMP_CHIMIQUE ).fillNodes (
                  new String[] {
                    new CustomSb (
                      "Inserer la premiere reponse pour " + COMP_CHIMIQUE
                    ).out()
                  }
                )
              }
            ),
          }
            /*
          new Node[] {
            new Node(STOCH).fillNodes (
              new Node[] {
                new Node ( "Qu'est-ce que la stochiometrie?" ).fillNodes(
                  new String[] {
                    new CustomSb ("la stochiometrie est une analyse des quantites des particules ")
                      .append ( " dans une reaction chimique").out(),
                  }
                ),

                new Node ( "Qui est l'inventeur de la stochiometrie?").fillNodes (
                  new String[] {
                    new CustomSb ( "Jeremias Benjamin Richter est le premier homme a enoncer ")
                      .append ( "les principes de la stochiometrie.").out(),
                  }
                ),

                new Node ( "Quelles sont les applications de la stochiometrie?" ).fillNodes(
                  new String[] {
                    new CustomSb( "Voici quelques unes des applications de la **stochiometrie**").appendN("").out(),
                    new CustomSb( "- La pharmaceutique⚕" )
                      .appendN(
                        "- L'ingenieurie chimique👩‍🔬"
                      ).appendN(
                        "- Les sciences de l'environnement🌳"
                      ).out(),
                  }
                ),
              },
            ),
          }
          */
         // TODO: COMPLETE THIS STUFF
                /*
                new Node(COMP_CHIMIQUE).fillNodes (
                
                ),
                new Node(STRUCT_ATOMIQUE).fillNodes(

                ),
                new Node(REACT_CHIMIQUE).fillNodes(

                ),
                new Node(ATTRACTION_MOL ).fillNodes(

                ),*/
        );

        nodes_[2] = new Node ( PHYS );
        nodes_[2].fillNodes ( new Node[] {
            new Node (""),
        });

        return new NavTree( nodes_ );
    }

    public NavTree( Node[] nodes ) { 
        this.nodes = nodes;
    }

    public NavTree navigate( String content )
    {
        for ( int i = 0 ; i < this.nodes.length; i ++ )
        {
            if ( content.equals ( this.nodes[i].header ) )
            { return new NavTree(this.nodes[i].getNodes()); }
            else continue;
        } 


        return null;
    }


    @Override
    public String toString() { return getListNodes().toString(); }

    public static List<String> static_cast ( List<Node> tt )
    {
        List<String> ret = new ArrayList<String>(tt.size());
        for ( Node t : tt )
        { ret.add ( t.toString() ); }

        return ret;
    } 
    public List<Node> getListNodes () { 
        List<Node> ret = new ArrayList<Node>();
        for ( Node n : this.nodes ) { ret.add ( n ); }
        return ret;
    }

}
